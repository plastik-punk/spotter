from faker import Faker
import random

fake = Faker()

# Open a file to write
with open('insert_user_data.sql', 'w') as file:
    file.write("MERGE INTO app_user (id, first_name, last_name, email, mobile_number, password, role) VALUES\n")
    # Initialize an empty list to hold each entry
    entries = []
    x = 200
    entries.append(
        f"(0,'WalkIn','Customers','walk-in-customers@example.com',Null,'$2a$10$bTf7PeN.tyeHuyA3rh54JuSZP7E40RQhiPSeY3PdcJjMHvyN/nUWq',3)")
    for i in range(x):
        user_id = (i + 1) * -1
        first_name = fake.first_name()
        last_name = fake.last_name()
        email = f"{first_name.lower()}.{last_name.lower()}@example.com"  # formatted email to be lowercase
        # Generate phone number or NULL based on new condition
        if i % 6 == 0:
            phone = 'NULL'
        else:
            phone = ''.join(
                [str(random.randint(0, 9)) for _ in range(random.randint(8, 15))])  # Random number length 8-15
        role = random.choice([2, 3])
        if role == 3:
            password = '$2b$10$snk5o..7Ng1Spf.Ng8cf8.gy1XJYPXACM3nGYDLboG31VcxkOw02O'  # guest
        else:
            password = '$2a$10$bTf7PeN.tyeHuyA3rh54JuSZP7E40RQhiPSeY3PdcJjMHvyN/nUWq'  # password123

        # Create the line without adding a comma at the end
        line = f"({user_id}, '{first_name}', '{last_name}', '{email}', {phone}, '{password}', {role})"
        entries.append(line)

    while (x < 220):
        user_id = x * -1
        first_name = fake.first_name()
        last_name = fake.last_name()
        email = f"{first_name.lower()}.{last_name.lower()}@example.com"  # formatted email to be lowercase
        # Generate phone number or NULL based on new condition
        if i % 6 == 0:
            phone = 'NULL'
        else:
            phone = ''.join(
                [str(random.randint(0, 9)) for _ in range(random.randint(8, 15))])  # Random number length 8-15
        if x % 3 == 0:
            role = random.choice([0, 4, 5])
        else:
            role = 1
        password = '$2a$10$bTf7PeN.tyeHuyA3rh54JuSZP7E40RQhiPSeY3PdcJjMHvyN/nUWq'  # password123

        # Create the line without adding a comma at the end
        line = f"({user_id}, '{first_name}', '{last_name}', '{email}', {phone}, '{password}', {role})"
        entries.append(line)
        x += 1

    # Join all entries with a comma and new line, and add a semicolon at the end
    file.write(",\n".join(entries) + ";\n")

print("SQL insert script generated successfully.")
