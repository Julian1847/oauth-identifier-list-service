Vorbedingungen:
Python 3.13.1 über PATH verfügbar machen.
JAVA 17.0.12



Um den Service zu starten, sodass er funktioniert 
bitte folgendes tun:

1. Die Postgres-Datenbank in der docker-compose.yml starten. 
2. IdentifierListServiceApp starten.


DUMMY-DATEN:
1. "pip install psycopg2-binary" ausführen.
2. Um Dummy Daten hinzuzufügen "insert_identifiers.py" ausführen. 
3. Falls es zu viele Dummy Daten sind, das Skript an den entsprechenden Stellen anpassen.