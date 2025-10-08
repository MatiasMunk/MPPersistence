-- ==================================
-- Insert CustomerDetails
-- ==================================
INSERT INTO dbo.CustomerDetails (zipcode, city)
VALUES
('2100', 'Copenhagen'),
('5000', 'Odense');

-- ==================================
-- Insert Customers
-- ==================================
INSERT INTO dbo.Customer (phoneNo, name, address, zipCity_FK, type)
VALUES
('11111111', 'Copenhagen Sailing Club', 'Havnevej 12', '2100', 'klub'),
('22222222', 'John Doe', 'Street 45', '5000', 'privat');

-- ==================================
-- Insert SupplierDetails
-- ==================================
INSERT INTO dbo.SupplierDetails (country, address)
VALUES
('Denmark', 'Industrivej 15');

-- ==================================
-- Insert Supplier
-- ==================================
INSERT INTO dbo.Supplier (phoneNo, name, address, country_FK, email)
VALUES
('33333333', 'Nordic Music Supply', 'Industrivej 15', 'Denmark', 'contact@nordicmusic.dk');

-- ==================================
-- Insert Warehouse
-- ==================================
INSERT INTO dbo.Warehouse ([number], name, description)
VALUES
(1, 'Main Warehouse', 'Primary distribution center');

-- ==================================
-- Insert Products
-- ==================================

-- Music product
INSERT INTO dbo.Product (productNumber, name, minStock, supplierPhoneNo_FK)
VALUES (1001, '2Pac - Me Against The World', 10, '33333333');
INSERT INTO dbo.Music (productNumber_FK, format, artist)
VALUES (1001, 'Vinyl', '2Pac');

-- Equipment product
INSERT INTO dbo.Product (productNumber, name, minStock, supplierPhoneNo_FK)
VALUES (1002, 'Camping Tent 3-Person', 5, '33333333');
INSERT INTO dbo.Equipment (productNumber_FK, material, style)
VALUES (1002, 'Polyester', 'Outdoor');

-- Clothing product
INSERT INTO dbo.Product (productNumber, name, minStock, supplierPhoneNo_FK)
VALUES (1003, 'Leather Jacket', 15, '33333333');
INSERT INTO dbo.Clothing (productNumber_FK, size, color)
VALUES (1003, 'L', 'Black');

-- GunReplica product
INSERT INTO dbo.Product (productNumber, name, minStock, supplierPhoneNo_FK)
VALUES (1004, 'WWII Pistol Replica', 3, '33333333');
INSERT INTO dbo.GunReplica (productNumber_FK, calibre, material)
VALUES (1004, '9mm', 'Metal');

-- ==================================
-- Insert Stock
-- ==================================
INSERT INTO dbo.Stock (productNumber_FK, warehouseNumber_FK, availableQty, reservedQty)
VALUES
(1001, 1, 50, 0),
(1002, 1, 100, 0),
(1003, 1, 100, 0),
(1004, 1, 100, 0);

-- ==================================
-- Insert Sale Prices (optional but valid for this schema)
-- ==================================
INSERT INTO dbo.SalePrice (productNumber_FK, [timestamp], price)
VALUES
(1001, SYSUTCDATETIME(), 199.99),
(1002, SYSUTCDATETIME(), 899.50),
(1003, SYSUTCDATETIME(), 349.00),
(1004, SYSUTCDATETIME(), 1200.00);