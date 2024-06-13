import random
from datetime import datetime, timedelta
from faker import Faker

fake = Faker()

# Configuration for the script
num_reservations = 10000
num_places = 10  # Number of places to create
user_id_range = (-200, -1)
opening_hour = 11
closing_hour = 22
date_start = datetime.now().date() - timedelta(days=5 * 365)  # Approximately 5 years back
date_end = datetime.now().date() + timedelta(days=90)  # 3 months ahead
start_reservation_id = -1  # Assuming starting ID for reservation
start_place_id = -1  # Starting ID for place

# Dictionary to store place capacity and status
places = {}
reservations = {}


# Function to generate a random time within opening hours
def random_time():
    # Define two ranges of valid start times
    morning_range_start = 11
    morning_range_end = 15
    evening_range_start = 17
    evening_range_end = 22

    # Choose a range based on a random choice
    if random.choice([True, False]):
        hour = random.randint(morning_range_start, morning_range_end - 2)  # Ends at 13:00 latest for 2-hour slot
    else:
        hour = random.randint(evening_range_start, evening_range_end - 2)  # Ends at 20:00 latest for 2-hour slot

    minute = random.choice([0, 15, 30, 45])
    time = datetime.strptime(f"{hour}:{minute}", "%H:%M").time()

    # Adjust times to fit within the allowed booking slots
    if time.hour >= morning_range_end - 1:
        time = datetime.strptime(f"{morning_range_end - 2}:30", "%H:%M").time()
    elif time.hour >= evening_range_end - 1:
        time = datetime.strptime(f"{evening_range_end - 2}:30", "%H:%M").time()

    return time


# Generate SQL script
with open('insert_reservations_and_mapping.sql', 'w') as file:
    # Write SQL for inserting places
    file.write("MERGE INTO place (id, pax, status) VALUES\n")
    place_entries = []
    current_place_id = start_place_id
    for _ in range(num_places):
        pax = random.randint(4, 10)  # Random capacity for places, minimum 4 to ensure buffer
        status = random.choice([0, 1])  # Random status
        places[current_place_id] = {'pax': pax, 'status': status}
        status_value = '1' if status == 1 else '0'
        place_entries.append(f"({current_place_id}, {pax}, '{status_value}')")
        current_place_id -= 1
    file.write(",\n".join(place_entries) + ";\n\n")

    # Write SQL for inserting reservations
    file.write(
        "MERGE INTO reservation (id, user_id, date, start_time, end_time, pax, notes, hash_value,confirmed) VALUES\n")
    reservation_entries = []
    reservation_place_entries = []
    current_reservation_id = start_reservation_id
    for _ in range(num_reservations):
        attempt = 0
        max_attempts = 100  # Avoid infinite loop
        while attempt < max_attempts:
            attempt += 1
            user_id = random.randint(*user_id_range)
            place_id = random.randint(start_place_id - num_places + 1, start_place_id)
            date = date_start + timedelta(days=random.randint(0, (date_end - date_start).days))
            start_time = random_time()
            end_time = (datetime.combine(date, start_time) + timedelta(hours=2)).time()  # 2-hour duration
            pax = random.randint(1, places[place_id]['pax'] - 1)  # number of people, ensuring buffer
            notes = fake.sentence(nb_words=10)  # Simplified text generation for speed
            hash_value = fake.sha256()  # simulate a hash value

            # Check if the place is available and the pax does not exceed the capacity with buffer
            if places[place_id]['status'] == 1 and pax <= places[place_id]['pax'] - 1:
                # Check if there is an overlap
                overlap = False
                if place_id in reservations:
                    for existing in reservations[place_id]:
                        if existing['date'] == date and (
                            existing['start_time'] <= start_time < existing['end_time'] or existing[
                            'start_time'] < end_time <= existing['end_time']):
                            overlap = True
                            break

                if not overlap:
                    if place_id not in reservations:
                        reservations[place_id] = []
                    reservations[place_id].append({'date': date, 'start_time': start_time, 'end_time': end_time})
                    reservation_entry = f"({current_reservation_id}, {user_id}, '{date}', '{start_time}', '{end_time}', {pax}, '{notes}', '{hash_value}',{0})"
                    reservation_entries.append(reservation_entry)
                    # Add entry for reservationplace mapping table
                    reservation_place_entry = f"({current_reservation_id}, {place_id})"
                    reservation_place_entries.append(reservation_place_entry)
                    current_reservation_id -= 1
                    break  # Exit the while loop once a valid entry is created

    file.write(",\n".join(reservation_entries) + ";\n\n")
    file.write("MERGE INTO RESERVATION_PLACE (reservation_id, place_id) VALUES\n")
    file.write(",\n".join(reservation_place_entries) + ";\n")

print("SQL insert script for reservations and mappings generated successfully.")