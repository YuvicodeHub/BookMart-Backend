package com.yuvigupta2410.ShopApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin    // For connecting different port no. i.e frontend to backend

@RequestMapping("/api")
public class Controller {
        @Autowired
        private ProdService service ;

 @GetMapping("/products")
 public ResponseEntity<List<Product> > getAllProducts(){
      return  new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK)  ;
 }

 @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){
      return new ResponseEntity<>(service.getProductById(id), HttpStatus.OK);
 }
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile){
     try{
         Product product1 = service.addProduct(product, imageFile);
         return new ResponseEntity<>(product1, HttpStatus.CREATED);
     }
     catch (Exception e){
         return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR) ;
     }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){

     Product product = service.getProductById(productId);
     byte[] imageFile= product.getImageData() ;

     return ResponseEntity.ok()
             .body(imageFile);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product,
                                                @RequestPart MultipartFile imageFile){
        Product product1 = null;
        try {
            product1 = service.updateProduct(id, product, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(product1 != null){
         return new ResponseEntity<>("Updated Sucessfully!!", HttpStatus.OK) ;
     }else{
         return new ResponseEntity<>("Failed to Update",HttpStatus.BAD_REQUEST);
     }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product = service.getProductById(id);
        if(product != null){
            service.deleteProduct(id);
            return new ResponseEntity<>("Item is Deleted", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Item is not found", HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/product/search")
//    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
//        List<Product> products = service.searchProducts(keyword);
//
//        return new ResponseEntity<>(products, HttpStatus.OK);
//    }
}
