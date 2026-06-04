CREATE USER fernandez_biblioteca_user WITH PASSWORD 'fernandez_biblioteca_pass';
CREATE DATABASE fernandez_biblioteca_db OWNER fernandez_biblioteca_user;
GRANT ALL PRIVILEGES ON DATABASE fernandez_biblioteca_db TO fernandez_biblioteca_user;
\q

uv add django djangorestframework djangorestframework-simplejwt django-filter django-cors-headers psycopg2-binary python-decouple

New-Item -ItemType Directory -Force biblioteca\models, biblioteca\serializers, biblioteca\views, biblioteca\tests

$files = @(
    "biblioteca\models\__init__.py",
    "biblioteca\serializers\__init__.py",
    "biblioteca\views\__init__.py",
    "biblioteca\tests\__init__.py",
    "biblioteca\filters.py",
    "biblioteca\permissions.py"
)
$files | ForEach-Object { New-Item -ItemType File -Force $_ }