import random
from datetime import datetime, timedelta
from faker import Faker

fake = Faker()

# Configuration for the script
num_reservations = 30000
num_places = 23  # Number of places to create
user_id_range = (-200, -1)
opening_hour = 11
closing_hour = 22
date_start = datetime.now().date() - timedelta(days=5 * 365)  # Approximately 5 years back
date_end = datetime.now().date() + timedelta(days=90)  # 3 months ahead
start_reservation_id = -1  # Assuming starting ID for reservation
start_place_id = 1  # Starting ID for place
start_area_id = -1
start_segment_id = -1

# Storage for generated data
places = {}
reservations = {}
areas = {}
segments = {}
area_place_segments = []

# Define areas as per Java generator logic
areas[start_area_id] = {'name': "Main Area", 'width': 15, 'height': 8, 'is_open': True}
areas[start_area_id - 1] = {'name': "Second Area", 'width': 19, 'height': 4, 'is_open': False}

# Define segments from your Java generator, exact numbers if specified or pattern repeated
for i in range(30):  # Example of having 30 segments with specific logic or unique ids
    segments[start_segment_id - i] = {'x': i % 10,
                                      'y': i // 10}  # Example pattern; adjust as needed from Java generator


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
with open('insert_data_script.sql', 'w') as file:
    # Write SQL for inserting places
    file.write("MERGE INTO place (id, pax, status,number) VALUES\n")
    place_entries = []
    current_place_id = start_place_id
    for _ in range(num_places):
        pax = random.randint(2, 5) + current_place_id % 4
        places[current_place_id] = {'pax': pax}
        status_value = '1'
        tableNumber = current_place_id;
        place_entries.append(f"({current_place_id}, {pax}, '{status_value}','{tableNumber}')")
        current_place_id += 1
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
            place_id = random.randint(start_place_id, start_place_id + num_places - 1, )
            date = date_start + timedelta(days=random.randint(0, (date_end - date_start).days))
            start_time = random_time()
            end_time = (datetime.combine(date, start_time) + timedelta(hours=2)).time()  # 2-hour duration
            pax = random.randint(1, places[place_id]['pax'])  # number of people, ensuring buffer
            notes = fake.sentence(nb_words=10)  # Simplified text generation for speed
            hash_value = fake.sha256()  # simulate a hash value

            # Check if the place is available and the pax does not exceed the capacity with buffer
            if pax <= places[place_id]['pax']:
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

        if (current_reservation_id + 1) % 1000 == 0:
            print(f"{current_reservation_id + 1} reservations generated.")
    file.write(",\n".join(reservation_entries) + ";\n\n")
    file.write("MERGE INTO RESERVATION_PLACE (reservation_id, place_id) VALUES\n")
    file.write(",\n".join(reservation_place_entries) + ";\n")
    # # Areas
    # file.write("MERGE INTO area (id, name, width, height, is_open) VALUES\n")
    # area_entries = [
    #     f"({aid}, '{adata['name']}', {adata['width']}, {adata['height']}, {'TRUE' if adata['is_open'] else 'FALSE'})"
    #     for aid, adata in areas.items()]
    # file.write(",\n".join(area_entries) + ";\n\n")
    #
    # # Segments
    # file.write("MERGE INTO segment (id, x1, y1) VALUES\n")
    # segment_entries = [f"({sid}, {sdata['x']}, {sdata['y']})" for sid, sdata in segments.items()]
    # file.write(",\n".join(segment_entries) + ";\n\n")
    #
    # # Generating AreaPlaceSegment Mappings based on specific patterns
    # aps_data = [
    #     (1, 1, [1, 2]),
    #     (1, 2, [3, 4]),
    #     (1, 3, [5, 6]),
    #     (1, 4, [7, 8]),
    #     (1, 5, [9, 10]),
    #     (1, 6, [11, 12]),
    #     (1, 7, [13, 14]),
    #     (1, 8, [15, 16]),
    #     (1, 9, [17, 18]),
    #     (1, 10, [19]),
    #     (1, 11, [20, 21, 22, 23]),
    #     (1, 13, [24]),
    #     (1, 14, [25]),
    #     (1, 16, [26, 27, 28, 29, 30, 31, 32, 33, 34]),
    #     (2, 17, [35, 36]),
    #     (2, 18, [37, 38]),
    #     (2, 19, [39, 40]),
    #     (2, 20, [41, 42]),
    #     (2, 21, [43, 44]),
    #     (2, 22, [45, 46]),
    #     (2, 23, [47, 48])
    # ]
    #
    # area_place_segments = []
    # for area, place, segments in aps_data:
    #     for seg in segments:
    #         # Format the entries with negative place_id and segment_id directly in the string
    #         area_place_segments.append((f"-{area}", place, f"-{seg}"))
    #
    # # Writing to the SQL script file
    # file.write("MERGE INTO area_place_segment (area_id, place_id, segment_id) VALUES\n")
    # aps_entries = [f"({aps[0]}, {aps[1]}, {aps[2]})" for aps in area_place_segments]
    # file.write(",\n".join(aps_entries) + ";\n")
print("SQL insert script for reservations and mappings generated successfully.")
