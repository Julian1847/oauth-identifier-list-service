Vorbedingungen:
Python 3.13.1 über PATH verfügbar machen.
JAVA 17.0.12



Um den Service zu starten, sodass er funktioniert 
bitte folgendes tun:

1. Die Datenbank in der docker-compose.yml starten. 
2. IdentifierListServiceApp starten.
3. "pip install psycopg2-binary" ausführen.
4. Um Dummy Daten hinzuzufügen "insert_identifiers.py" ausführen. 
5. Falls es zu viele Dummy Daten sind, das Skript an den entsprechenden Stellen anpassen.