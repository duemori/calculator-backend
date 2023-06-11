CREATE DATABASE calculator;
CREATE DATABASE person;

CREATE TABLE calculator.operations (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(20) NOT NULL,
	description VARCHAR(20) NOT NULL,
	params TINYINT NOT NULL,
	cost DECIMAL(5,2) NOT NULL,
	PRIMARY KEY (id)
);

INSERT INTO calculator.operations (id, name, description, params, cost) VALUES (1, 'ADDITION', 'Addition', 2, 0.5);
INSERT INTO calculator.operations (id, name, description, params, cost) VALUES (2, 'SUBTRACTION', 'Subtraction', 2, 0.6);
INSERT INTO calculator.operations (id, name, description, params, cost) VALUES (3, 'MULTIPLICATION', 'Multiplication', 2, 1.2);
INSERT INTO calculator.operations (id, name, description, params, cost) VALUES (4, 'DIVISION', 'Division', 2, 1.7);
INSERT INTO calculator.operations (id, name, description, params, cost) VALUES (5, 'SQUARE_ROOT', 'Square root', 1, 2.3);
INSERT INTO calculator.operations (id, name, description, params, cost) VALUES (6, 'RANDOM_STRING', 'Random string', 0, 3.5);

CREATE TABLE calculator.transactions (
	id INT NOT NULL AUTO_INCREMENT,
	operations_id INT NULL,
	users_id INT NOT NULL,
	amount DECIMAL(9,2) NOT NULL,
	credit_debit CHAR(1) NOT NULL,
	params VARCHAR(25) NULL,
	response VARCHAR(20) NULL,
	date DATETIME NOT NULL,
	active BOOLEAN NOT NULL DEFAULT 1,
	PRIMARY KEY (id),
	FOREIGN KEY (operations_id) REFERENCES operations(id),
	INDEX transactions_users_id_idx (users_id)
);

CREATE TABLE person.users (
	id INT NOT NULL AUTO_INCREMENT,
	email VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	date DATETIME NOT NULL,
	active BOOLEAN NOT NULL DEFAULT 1,
	PRIMARY KEY (id),
	INDEX users_email_idx (email)
);

INSERT INTO person.users (email, password, date, active) VALUES ('test@test.com','{bcrypt}$2a$10$NpmqIXiJ4ZTra.btzM/bZ.DS7OnvO/BnkEiSnpT9sO.RINPNu/Td2',NOW(),true);

CREATE USER 'calculator'@'%' IDENTIFIED BY 'calc123';
GRANT ALL PRIVILEGES ON calculator.* TO 'calculator'@'%';

CREATE USER 'person'@'%' IDENTIFIED BY 'person123';
GRANT ALL PRIVILEGES ON person.* TO 'person'@'%';