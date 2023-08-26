CREATE TABLE receipts (
    id UUID PRIMARY KEY,
    retailer VARCHAR(255) NOT NULL,
    purchaseDate DATE NOT NULL,
    purchaseTime TIME NOT NULL,
    total DECIMAL(10, 2) NOT NULL
);

CREATE TABLE receipt_items (
    receipt_id UUID NOT NULL,
    shortDescription VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (receipt_id) REFERENCES receipts(id) ON DELETE CASCADE
);
