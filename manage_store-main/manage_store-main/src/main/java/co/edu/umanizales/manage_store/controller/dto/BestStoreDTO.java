package co.edu.umanizales.manage_store.controller.dto;

import co.edu.umanizales.manage_store.model.Store;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BestStoreDTO {
    private Store store;
    private int quantity;
}
