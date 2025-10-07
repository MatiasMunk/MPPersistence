-- ==================================
-- Insert Customers
-- ==================================
INSERT INTO dbo.Customer (phoneNo, name, address, zipcode, city, type)
VALUES
('11111111', 'Copenhagen Sailing Club', 'Havnevej 12', '2100', 'Copenhagen', 'klub'),
('22222222', 'John Doe', 'Street 45', '5000', 'Odense', 'privat');

-- ==================================
-- Insert Supplier
-- ==================================
INSERT INTO dbo.Supplier (phoneNo, name, address, country, email)
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
-- Example 1: a music album (art-related product)
INSERT INTO dbo.Product (
    productNumber, name, minStock, supplierPhoneNo_FK,
    format, artist, size, colour, eqpmtMaterial, style, calibre, gnMaterial
)
VALUES
(1001, '2Pac - Me Against The World', 10, '33333333',
 'Vinyl', '2Pac', '12-inch', NULL, NULL, 'Hip-Hop', NULL, NULL);

-- Example 2: an equipment item (material-related product)
INSERT INTO dbo.Product (
    productNumber, name, minStock, supplierPhoneNo_FK,
    format, artist, size, colour, eqpmtMaterial, style, calibre, gnMaterial
)
VALUES
(1002, 'Camping Tent 3-Person', 5, '33333333',
 NULL, NULL, '3-person', 'Green', 'Polyester', NULL, NULL, NULL);

-- Example 3: a clothing item 
INSERT INTO dbo.Product (
    productNumber, name, minStock, supplierPhoneNo_FK,
    format, artist, size, colour, eqpmtMaterial, style, calibre, gnMaterial
)
VALUES
(1003, 'Leather Jacket', 15, '33333333',
 NULL, NULL, 'L', 'Black', 'Leather', 'Casual', NULL, NULL);

-- Example 4: a GunReplica item
INSERT INTO dbo.Product (
    productNumber, name, minStock, supplierPhoneNo_FK,
    format, artist, size, colour, eqpmtMaterial, style, calibre, gnMaterial
)
VALUES
(1004, 'WWII Pistol Replica', 3, '33333333',
 NULL, NULL, 'Standard', 'Metallic', 'Metal', 'Replica', '9mm', NULL);

-- ==================================
-- Insert Stock
-- ==================================
INSERT INTO dbo.Stock (productNumber_FK, warehouseNumber_FK, availableQty, reservedQty)
VALUES
(1001, 1, 50, 5),
(1002, 1, 100, 10),
(1003, 1, 100, 10),
(1004, 1, 100, 10);