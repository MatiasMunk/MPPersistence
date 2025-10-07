USE PersistenceTest

GO

-- ===============================
-- Sales Management Schema (MSSQL)
-- ===============================

IF OBJECT_ID('dbo.Discount', 'U') IS NOT NULL DROP TABLE dbo.Discount;
IF OBJECT_ID('dbo.Freight', 'U') IS NOT NULL DROP TABLE dbo.Freight;
IF OBJECT_ID('dbo.Invoice', 'U') IS NOT NULL DROP TABLE dbo.Invoice;
IF OBJECT_ID('dbo.OrderLineItem', 'U') IS NOT NULL DROP TABLE dbo.OrderLineItem;
IF OBJECT_ID('dbo.SalePrice', 'U') IS NOT NULL DROP TABLE dbo.SalePrice;
IF OBJECT_ID('dbo.Stock', 'U') IS NOT NULL DROP TABLE dbo.Stock;
IF OBJECT_ID('dbo.SaleOrder', 'U') IS NOT NULL DROP TABLE dbo.SaleOrder;
IF OBJECT_ID('dbo.GunReplica', 'U') IS NOT NULL DROP TABLE dbo.GunReplica;
IF OBJECT_ID('dbo.Equipment', 'U') IS NOT NULL DROP TABLE dbo.Equipment;
IF OBJECT_ID('dbo.Clothing', 'U') IS NOT NULL DROP TABLE dbo.Clothing;
IF OBJECT_ID('dbo.Music', 'U') IS NOT NULL DROP TABLE dbo.Music;
IF OBJECT_ID('dbo.Product', 'U') IS NOT NULL DROP TABLE dbo.Product;
IF OBJECT_ID('dbo.Warehouse', 'U') IS NOT NULL DROP TABLE dbo.Warehouse;
IF OBJECT_ID('dbo.Supplier', 'U') IS NOT NULL DROP TABLE dbo.Supplier;
IF OBJECT_ID('dbo.Customer', 'U') IS NOT NULL DROP TABLE dbo.Customer;

CREATE TABLE dbo.Customer (
    phoneNo    NVARCHAR(20) PRIMARY KEY,
    name       NVARCHAR(100) NOT NULL,
    address    NVARCHAR(200) NULL,
    zipcode    NVARCHAR(20)  NULL,
    city       NVARCHAR(100) NULL,
    type       NVARCHAR(50)  NULL
);

CREATE TABLE dbo.Supplier (
    phoneNo NVARCHAR(20) PRIMARY KEY,
    name    NVARCHAR(100) NOT NULL,
    address NVARCHAR(200) NULL,
    country NVARCHAR(100) NULL,
    email   NVARCHAR(100) NULL
);

CREATE TABLE dbo.Warehouse (
    [number] INT PRIMARY KEY,
    name     NVARCHAR(100) NOT NULL,
    description NVARCHAR(255) NULL
);

CREATE TABLE dbo.Product (
    productNumber INT PRIMARY KEY,
    name          NVARCHAR(100) NOT NULL,
    minStock      INT NULL,
    supplierPhoneNo_FK NVARCHAR(20) NULL,
    CONSTRAINT FK_Product_Supplier FOREIGN KEY (supplierPhoneNo_FK) REFERENCES dbo.Supplier(phoneNo)
);

CREATE TABLE dbo.Music (
    productNumber_FK INT PRIMARY KEY,
    format           NVARCHAR(100) NOT NULL,
    artist           NVARCHAR(100) NOT NULL,
    CONSTRAINT FK_MUSIC_PRODUCT FOREIGN KEY (productNumber_FK) REFERENCES dbo.Product(productNumber)
);

CREATE TABLE dbo.Clothing (
    productNumber_FK INT PRIMARY KEY,
    size             NVARCHAR(100) NOT NULL,
    color            NVARCHAR(100) NOT NULL,
    CONSTRAINT FK_CLOTHING_PRODUCT FOREIGN KEY (productNumber_FK) REFERENCES dbo.Product(productNumber)
);

CREATE TABLE dbo.Equipment (
    productNumber_FK INT PRIMARY KEY,
    material         NVARCHAR(100) NOT NULL,
    style            NVARCHAR(100) NOT NULL,
    CONSTRAINT FK_EQUIPMENT_PRODUCT FOREIGN KEY (productNumber_FK) REFERENCES dbo.Product(productNumber)
);

CREATE TABLE dbo.GunReplica (
    productNumber_FK INT PRIMARY KEY,
    calibre          NVARCHAR(100) NOT NULL,
    material         NVARCHAR(100) NOT NULL,
    CONSTRAINT FK_GUNREPLICA_PRODUCT FOREIGN KEY (productNumber_FK) REFERENCES dbo.Product(productNumber)
);

-- (composite PK ensures multiple products can have prices at the same timestamp)
CREATE TABLE dbo.SalePrice (
    productNumber_FK INT NOT NULL,
    [timestamp]      DATETIME NOT NULL DEFAULT (SYSUTCDATETIME()),
    price            DECIMAL(10,2) NOT NULL,
    CONSTRAINT PK_SalePrice PRIMARY KEY (productNumber_FK, [timestamp]),
    CONSTRAINT FK_SalePrice_Product FOREIGN KEY (productNumber_FK) REFERENCES dbo.Product(productNumber)
);

CREATE TABLE dbo.Stock (
    productNumber_FK   INT NOT NULL,
    warehouseNumber_FK INT NOT NULL,
    availableQty       INT NOT NULL DEFAULT(0),
    reservedQty        INT NOT NULL DEFAULT(0),
    CONSTRAINT PK_Stock PRIMARY KEY (productNumber_FK, warehouseNumber_FK),
    CONSTRAINT FK_Stock_Product FOREIGN KEY (productNumber_FK) REFERENCES dbo.Product(productNumber),
    CONSTRAINT FK_Stock_Warehouse FOREIGN KEY (warehouseNumber_FK) REFERENCES dbo.Warehouse([number])
);

CREATE TABLE dbo.SaleOrder (
    saleOrderID      INT IDENTITY(1,1) PRIMARY KEY,
    customerPhoneNo_FK NVARCHAR(20) NULL,
    [date]           DATE NOT NULL,
    amount           DECIMAL(10,2) NULL,
    deliveryStatus   NVARCHAR(50) NULL,
    deliveryDate     DATE NULL,
    CONSTRAINT FK_SaleOrder_Customer FOREIGN KEY (customerPhoneNo_FK) REFERENCES dbo.Customer(phoneNo)
);

CREATE TABLE dbo.OrderLineItem (
    saleOrderID_FK   INT NOT NULL,
    productNumber_FK INT NOT NULL,
    quantity         INT NOT NULL,
    CONSTRAINT PK_OrderLineItem PRIMARY KEY (saleOrderID_FK, productNumber_FK),
    CONSTRAINT FK_OrderLineItem_SaleOrder FOREIGN KEY (saleOrderID_FK) REFERENCES dbo.SaleOrder(saleOrderID),
    CONSTRAINT FK_OrderLineItem_Product FOREIGN KEY (productNumber_FK) REFERENCES dbo.Product(productNumber)
);

CREATE TABLE dbo.Invoice (
    saleOrderID_FK INT PRIMARY KEY,
    paymentDate    DATE NULL,
    amount         DECIMAL(10,2) NULL,
    vat            DECIMAL(10,2) NULL,
    totalAmount    DECIMAL(10,2) NULL,
    CONSTRAINT FK_Invoice_SaleOrder FOREIGN KEY (saleOrderID_FK) REFERENCES dbo.SaleOrder(saleOrderID)
);

CREATE TABLE dbo.Discount (
    saleOrderID_FK   INT NOT NULL,
    [type]           NVARCHAR(50) NOT NULL,
    amount           DECIMAL(10,2) NULL,
    discountThreshold DECIMAL(10,2) NULL,
    CONSTRAINT PK_Discount PRIMARY KEY (saleOrderID_FK, [type]),
    CONSTRAINT FK_Discount_SaleOrder FOREIGN KEY (saleOrderID_FK) REFERENCES dbo.SaleOrder(saleOrderID)
);

CREATE TABLE dbo.Freight (
    saleOrderID_FK INT PRIMARY KEY,
    [method]        NVARCHAR(100) NULL,
    baseCost        DECIMAL(10,2) NULL,
    freeThreshold   DECIMAL(10,2) NULL,
    CONSTRAINT FK_Freight_SaleOrder FOREIGN KEY (saleOrderID_FK) REFERENCES dbo.SaleOrder(saleOrderID)
);

-- ===============================
-- End of schema
-- ===============================