import psycopg2
import csv
import os
import random

db_params = {
    "dbname": "identifier_service",
    "user": "postgres",
    "password": "postgres",
    "host": "localhost",
    "port": "5432"
}

CSV_DIR = "./csvs/identifier"

def create_csv():
    try:
        conn = psycopg2.connect(**db_params)
        cursor = conn.cursor()

        if not os.path.exists(CSV_DIR):
            os.makedirs(CSV_DIR)

        cursor.execute("SELECT count(*) FROM identifier_lists;")
        total_lists = cursor.fetchone()[0]

        print(f"Es gibt insgesamt {total_lists} Listen in der Datenbank.")
        print(f"Erstelle CSV-Datei für Reqeusts des Holders...")
        createHolderReqeustCSV(total_lists)


        entries_to_create = 200
        list_ids = random.sample(range(1, total_lists + 1), random.randint(1, total_lists))

        print(f"Ausgewählte Listen-IDs: {list_ids}")
        print(f"Erstelle CSV-Datei im Verzeichnis {CSV_DIR}...")


        csv_file_path = os.path.join(CSV_DIR, "identifier_list_revocation_parameters.csv")
        with open(csv_file_path, mode='w', newline='') as file:
            writer = csv.writer(file)
            writer.writerow(["id", "uri"])

            entries_added = 0
            for list_id in list_ids:
                remaining_entries = entries_to_create - entries_added
                num_identifiers = random.randint(1, min(remaining_entries, 5))

                cursor.execute("""
                    SELECT id FROM identifiers WHERE list_id = %s ORDER BY RANDOM() LIMIT %s;
                """, (list_id, num_identifiers))

                identifiers = cursor.fetchall()

                for identifier in identifiers:
                    identifier_uuid = identifier[0]
                    uri = f"http://localhost:8085/identifier-list/{list_id}"
                    writer.writerow([identifier_uuid, uri])

                entries_added += len(identifiers)
                if entries_added >= entries_to_create:
                    break

            print(f"CSV-Datei erfolgreich erstellt: {csv_file_path}")

        cursor.close()
        conn.close()
        print("CSV-Erstellung abgeschlossen.")

    except Exception as e:
        print("Fehler beim Erstellen der CSV-Datei:", e)

def createHolderReqeustCSV(total_lists):
    number_of_entries = min(total_lists, 250)

    csv_file_path = os.path.join(CSV_DIR, "holder_request_parameters_identifier_list.csv")
    with open(csv_file_path, mode='w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(["id"])
        for i in range(1, number_of_entries + 1):
            random_number = random.randint(1, total_lists)
            writer.writerow([random_number])

    print(f"CSV-Erstellung für Holder mit {number_of_entries} abgeschlossen.")


if __name__ == "__main__":
    create_csv()
