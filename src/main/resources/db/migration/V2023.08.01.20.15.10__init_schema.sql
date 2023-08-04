-- V2023.08.01.20.15.10__init_schema.sql

CREATE TABLE member (
        MEMBER_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
        email VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL,
        type VARCHAR(255) NOT NULL,
        nickName VARCHAR(255) NOT NULL,
        name VARCHAR(255) NOT NULL,
        picture VARCHAR(255),
        role VARCHAR(50) NOT NULL
);

CREATE INDEX index_member_email ON member (email);