package co.edu.umanizales.manage_store.controller;

import co.edu.umanizales.manage_store.controller.dto.ResponseDTO;
import co.edu.umanizales.manage_store.controller.dto.SaleDTO;
import co.edu.umanizales.manage_store.model.Sale;
import co.edu.umanizales.manage_store.model.Seller;
import co.edu.umanizales.manage_store.model.Store;
import co.edu.umanizales.manage_store.service.SaleService;
import co.edu.umanizales.manage_store.service.SellerService;
import co.edu.umanizales.manage_store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "sale")
public class SaleController {
    @Autowired
    private SaleService saleService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private StoreService storeService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getSales(){
        return new ResponseEntity<>(
                new ResponseDTO(200,
                        saleService.getSales(),
                null),
                HttpStatus.OK);
    }
    @GetMapping(path="/total")
    public ResponseEntity<ResponseDTO> getTotalSales(){
        return new ResponseEntity<>(
                new ResponseDTO(200, saleService.getTotalSales(),null),HttpStatus.OK);
    }

    @GetMapping(path="/total/{code}")
    public ResponseEntity<ResponseDTO> getTotalSalesBySeller(
            @PathVariable String code
    ){
        return new ResponseEntity<>(
                new ResponseDTO(200, saleService.getTotalSalesBySeller(code),null),HttpStatus.OK);
    }
    @GetMapping(path="/totalStore/{code}")
    public ResponseEntity<ResponseDTO> getTotalSalesByStore(
            @PathVariable String code
    ){
        return new ResponseEntity<>(
                new ResponseDTO(200, saleService.getTotalSalesByStore(code),null),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createSale(@RequestBody
                                                      SaleDTO saleDTO){
        Seller findSeller = sellerService.getSellerById(saleDTO.getSellerId());
        if( findSeller == null){
            return new ResponseEntity<>(new ResponseDTO(409,
                    "El vendedor ingresado no existe",null),
                    HttpStatus.BAD_REQUEST);
        }
        Store findStore = storeService.getStoreById(saleDTO.getStoreId());
        if( findStore == null){
            return new ResponseEntity<>(new ResponseDTO(409,
                    "La tienda ingresada no existe",null),
                    HttpStatus.BAD_REQUEST);
        }
        saleService.addSale(new Sale(findStore,findSeller,
                saleDTO.getQuantity()));
        return new ResponseEntity<>(new ResponseDTO(200,
                "Venta adicionada",null),
                HttpStatus.OK);
    }

    @GetMapping(path = "/bestSeller")
    public ResponseEntity<ResponseDTO> getBestSeller(){
        return new ResponseEntity<>( new ResponseDTO(
                200, (saleService.getBestSeller(sellerService.getSellers())),null),HttpStatus.OK);
    }
    @GetMapping(path = "/bestStore")
    public ResponseEntity<ResponseDTO> getBestStore(){
        return new ResponseEntity<>( new ResponseDTO(
                200, (saleService.getBestStore(storeService.getStores())),null),HttpStatus.OK);
    }

    @GetMapping(path = "/averagesalesbystore")
    public ResponseEntity<ResponseDTO> getAverageSalesByStore(){
      int findSale = saleService.getTotalSales();
      if (findSale != 0){
        return new ResponseEntity<>(new ResponseDTO(
                200, saleService.getTotalSales()/(float)storeService.getStores().size(),
                null),HttpStatus.OK);
      }
      else
          return new ResponseEntity<>(new ResponseDTO(
                  409, "No hay ventas", null),HttpStatus.BAD_REQUEST);

    }

    @GetMapping(path = "/averagesalesbyseller")
    public ResponseEntity<ResponseDTO> getAverageSalesBySeller(){
        int findSale = saleService.getTotalSales();
        if (findSale != 0){
        return new ResponseEntity<>(new ResponseDTO(
                200,
                saleService.getTotalSales()/(float)sellerService.getSellers().size(),
                null),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(new ResponseDTO(
                    409, "No hay ventas", null),HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/storeswhitmostxcant/{cant}")
    public ResponseEntity<ResponseDTO> getSalesByCant (@PathVariable int cant) {
        if (saleService.getStoresBySalesXCant(cant).isEmpty()) {
            return new ResponseEntity<>(new ResponseDTO(
                    404, "No existen tiendas para cumplir con el requisito pedido", null)
                    ,HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(new ResponseDTO(200,saleService.getStoresBySalesXCant(cant),null),HttpStatus.OK);
        }
    }
}

