CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    is_active BOOLEAN DEFAULT TRUE,
    adresse_livraison VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    email_verified BOOLEAN DEFAULT FALSE,
    last_login_date DATETIME,
    law_firm_id BIGINT,
    mot_de_passe VARCHAR(255) NOT NULL,
    nom VARCHAR(255),
    numero_de_telephone VARCHAR(20),
    photo VARCHAR(255),
    prenom VARCHAR(255),
    reset_code VARCHAR(10),
    role VARCHAR(20),
    verification_code VARCHAR(10)
);
