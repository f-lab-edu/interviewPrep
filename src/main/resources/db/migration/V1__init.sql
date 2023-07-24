-- V1__init.sql
drop table if exists member;

CREATE TABLE member (
        MEMBER_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
        email VARCHAR(255) NOT NULL,
        password VARCHAR(255),
        type VARCHAR(255),
        nickName VARCHAR(255),
        name VARCHAR(255),
        picture VARCHAR(255),
        role VARCHAR(50) NOT NULL
);

CREATE INDEX i_member ON member (email);