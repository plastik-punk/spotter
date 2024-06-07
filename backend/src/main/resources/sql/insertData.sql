MERGE INTO app_user (id, first_name, last_name, email, mobile_number, password, role)
    VALUES (-1, 'Alice', 'Smith', 'alice.smith@example.com', '1234567890', 'securePassword1', '0'),
           (-2, 'Bob', 'Jones', 'bob.jones@example.com', '0987654321', 'securePassword2', '1'),
           (-3, 'Charlie', 'Brown', 'charlie.brown@example.com', '1122334455', 'securePassword3', '2'),
           (-4, 'Diana', 'Prince', 'diana.prince@example.com', NULL, 'securePassword4', '3'),
           (-5, 'Eve', 'White', 'eve.white@example.com', '1223334444', 'securePassword5', '4');