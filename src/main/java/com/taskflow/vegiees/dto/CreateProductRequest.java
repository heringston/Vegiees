package com.taskflow.vegiees.dto;

import com.taskflow.vegiees.entity.PriceUnit;
import com.taskflow.vegiees.entity.ProductCategory;
import com.taskflow.vegiees.entity.QuantityUnit;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateProductRequest {

    @NotBlank(message = "Product title is required")
    @Size(max = 150, message = "Title cannot exceed 150 characters")
    private String title;

    @NotNull(message = "Product category is required")
    private ProductCategory category;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Quantity can have at most 2 decimal places")
    private BigDecimal quantity;

    @NotNull(message = "Quantity Unit is required")
    private QuantityUnit quantityUnit;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    @Digits(integer = 8, fraction = 2, message = "Price can have at most 2 decimal places")
    private BigDecimal price;

    @NotNull(message = "Price unit is required")
    private PriceUnit priceUnit;

    @PastOrPresent(message = "Harvest date cannot be in the future")
    private LocalDate harvestDate;

    @NotBlank(message = "Pickup address is required")
    @Size(max = 300)
    private String pickupAddressLine;

    @NotBlank(message = "Pickup city is required")
    @Size(max = 100)
    private String pickupCity;

    @NotBlank(message = "Pickup district is required")
    @Size(max = 100)
    private String pickupDistrict;

    @NotBlank(message = "Pickup state is required")
    @Size(max = 100)
    private String pickupState;

    @NotBlank(message = "Pickup pincode is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "Enter a valid 6-digit pincode")
    private String pickupPincode;

    private boolean saveAddressToProfile;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public QuantityUnit getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(QuantityUnit quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public PriceUnit getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(PriceUnit priceUnit) {
        this.priceUnit = priceUnit;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(LocalDate harvestDate) {
        this.harvestDate = harvestDate;
    }

    public String getPickupAddressLine() {
        return pickupAddressLine;
    }

    public void setPickupAddressLine(String pickupAddressLine) {
        this.pickupAddressLine = pickupAddressLine;
    }

    public String getPickupCity() {
        return pickupCity;
    }

    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    public String getPickupDistrict() {
        return pickupDistrict;
    }

    public void setPickupDistrict(String pickupDistrict) {
        this.pickupDistrict = pickupDistrict;
    }

    public String getPickupState() {
        return pickupState;
    }

    public void setPickupState(String pickupState) {
        this.pickupState = pickupState;
    }

    public String getPickupPincode() {
        return pickupPincode;
    }

    public void setPickupPincode(String pickupPincode) {
        this.pickupPincode = pickupPincode;
    }

    public boolean isSaveAddressToProfile() {
        return saveAddressToProfile;
    }

    public void setSaveAddressToProfile(boolean saveAddressToProfile) {
        this.saveAddressToProfile = saveAddressToProfile;
    }
}
