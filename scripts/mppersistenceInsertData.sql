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
-- Insert Products (base)
-- ==================================
INSERT INTO dbo.Product (productNumber, name, minStock, supplierPhoneNo_FK)
VALUES
(1001, '2Pac - Me Against The World', 10, '33333333'),
(1002, 'Camping Tent 3-Person', 5, '33333333'),
(1003, 'Leather Jacket', 15, '33333333'),
(1004, 'WWII Pistol Replica', 3, '33333333');

-- ==================================
-- Insert Subclass Data
-- ==================================

-- Music
INSERT INTO dbo.Music (productNumber_FK, format, artist)
VALUES (1001, 'Vinyl', '2Pac');

-- Equipment
INSERT INTO dbo.Equipment (productNumber_FK, material, style)
VALUES (1002, 'Polyester', 'Outdoor');

-- Clothing
INSERT INTO dbo.Clothing (productNumber_FK, size, color)
VALUES (1003, 'L', 'Black');

-- GunReplica
INSERT INTO dbo.GunReplica (productNumber_FK, calibre, material)
VALUES (1004, '9mm', 'Metal');

-- ==================================
-- Insert Stock
-- ==================================
INSERT INTO dbo.Stock (productNumber_FK, warehouseNumber_FK, availableQty, reservedQty)
VALUES
(1001, 1, 50, 5),
(1002, 1, 100, 10),
(1003, 1, 100, 10),
(1004, 1, 100, 10);