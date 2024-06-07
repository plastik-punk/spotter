CREATE TABLE IF NOT EXISTS app_user (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          first_name VARCHAR(255) NOT NULL CHECK (first_name ~ '^[A-Za-zäöüÄÖÜß ]+$'),
                          last_name VARCHAR(255) NOT NULL CHECK (last_name ~ '^[A-Za-zäöüÄÖÜß ]+$'),
                          email VARCHAR(255) NOT NULL UNIQUE CHECK (email ~ '^[^@]+@[^@]+\.[^@]+$'),
                          mobile_number VARCHAR(15),
                          password VARCHAR(255) NOT NULL,
                          role VARCHAR(255) NOT NULL CHECK (role IN ('ADMIN', 'USER', 'GUEST')) -- Ensure your RoleEnum matches these values
);
