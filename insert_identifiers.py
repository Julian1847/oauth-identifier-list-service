import psycopg2
import uuid
from psycopg2.extras import execute_values

db_params = {
    "dbname": "identifier_service",
    "user": "postgres",
    "password": "postgres",
    "host": "localhost",
    "port": "5432"
}
# 51_114_726
TOTAL_IDENTIFIERS = 100_000
IDENTIFIERS_PER_LIST = 100_000
TOTAL_LISTS = TOTAL_IDENTIFIERS // IDENTIFIERS_PER_LIST
BATCH_SIZE = 10_000

def insert_data():
    try:
        conn = psycopg2.connect(**db_params)
        cursor = conn.cursor()

        print("Erstelle Identifier Lists...")
        list_ids = []
        cursor.execute("BEGIN;")
        for _ in range(TOTAL_LISTS):
            cursor.execute("INSERT INTO identifier_lists DEFAULT VALUES RETURNING id;")
            list_ids.append(cursor.fetchone()[0])
        conn.commit()
        print(f"{TOTAL_LISTS} Identifier Lists erstellt.")

        print("Füge Identifier hinzu...")
        for list_id in list_ids:
            identifiers = [(str(uuid.uuid4()), list_id, 0, 0) for _ in range(IDENTIFIERS_PER_LIST)]
            for i in range(0, IDENTIFIERS_PER_LIST, BATCH_SIZE):
                execute_values(cursor, """
                    INSERT INTO identifiers (id, list_id, status, status_change_count)
                    VALUES %s
                """, identifiers[i:i+BATCH_SIZE])
                conn.commit()
            print(f"{IDENTIFIERS_PER_LIST} Identifier zur Liste {list_id} hinzugefügt.")
    
        cursor.close()
        conn.close()
        print("Daten erfolgreich eingefügt.")
    except Exception as e:
        print("Fehler beim Einfügen der Daten:", e)

if __name__ == "__main__":
    insert_data()
