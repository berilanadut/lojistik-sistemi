# Project Details: Lojistik Yönetim Sistemi (Logistics Management System)

This document provides a comprehensive overview of the **Lojistik Yönetim Sistemi** (Logistics Management System), a Java-based web application tailored for managing logistics operations (under the context of Horoz Lojistik). 

This project is built as a lightweight monolithic application with a custom Java HTTP backend, a SQLite database, and a vanilla HTML/CSS/JavaScript frontend. It exposes multiple REST APIs to perform CRUD operations on cargo, stocks, transits, and employee overtime logs.

---

## 1. Technology Stack

- **Backend Language:** Java 23 (defined in `pom.xml` compiler source/target)
- **Web Server:** Custom embedded `com.sun.net.httpserver.HttpServer` running on port `8080`
- **Database:** SQLite (file-based database `cargo.db` in the root folder)
- **Database Connectivity:** SQLite JDBC (`org.xerial:sqlite-jdbc:3.46.1.0`)
- **JSON Serialization/Deserialization:** Google Gson (`com.google.code.gson:gson:2.14.0`)
- **Logging:** Logback Classic (`ch.qos.logback:logback-classic:1.2.13`)
- **Frontend:** Vanilla HTML5, CSS3, and JavaScript (AJAX operations using native browser `fetch` API)

---

## 2. Directory Structure

```text
lojistik-sistemi/
├── pom.xml                      # Maven project configuration
├── cargo.db                     # SQLite database file (generated at runtime)
└── src/
    └── main/
        ├── java/                # Java Source Files
        │   ├── Main.java        # Server entry point, route definitions, API handlers
        │   ├── Database.java    # Database connection & table creation initialization
        │   ├── Cargo.java       # Model: Cargo (Shipments)
        │   ├── CargoDAO.java    # DAO: Cargo database CRUD operations
        │   ├── CargoGecmis.java # Model: Cargo History Log
        │   ├── CargoGecmisDAO.java # DAO: Cargo History tracking
        │   ├── Urun.java        # Model: Urun (Warehouse Stock Item)
        │   ├── UrunDAO.java     # DAO: Stock database CRUD operations
        │   ├── Ulasim.java      # Model: Ulasim (Transportation / Transit Routing)
        │   ├── UlasimDAO.java   # DAO: Transportation database CRUD operations
        │   ├── Mesai.java       # Model: Mesai (Overtime log)
        │   └── MesaiDAO.java    # DAO: Overtime database CRUD operations
        └── resources/           # Frontend static resources served by the server
            ├── index.html       # Home Dashboard (Navigation Panel)
            ├── kargo.html       # Cargo Management Dashboard
            ├── kargo-detay.html # Cargo Details & History Viewer Dashboard
            ├── stok.html        # Warehouse Stock Control Dashboard
            ├── ulasim.html      # Transit Route Management Dashboard
            ├── mesai.html       # Employee Overtime Tracking Dashboard
            ├── style.css        # Shared stylesheet for all pages
            ├── script.js        # Main frontend script (AJAX operations)
            └── kargo-detay.js   # Script specific to Cargo details page
```

---

## 3. Database Schema & Data Models

The database consists of 5 main tables created dynamically in `Database.java` on application startup:

### A. `cargo` (Cargo Shipments)
Tracks active cargo shipments.
- **`kargo_no` (TEXT - PRIMARY KEY):** Unique tracking number.
- **`gonderici` (TEXT - NOT NULL):** Sender name/firm.
- **`alici` (TEXT - NOT NULL):** Receiver name.
- **`gonderici_sube` (TEXT):** Origin branch.
- **`teslimat_sube` (TEXT):** Destination branch.
- **`desi` (REAL):** Volumetric weight (decimeters cubed / 3000).
- **`agirlik` (REAL):** Actual weight in kg.
- **`durum` (TEXT):** Delivery status (e.g., "Hazırlanıyor", "Yolda", "Teslim Edildi").
- **`verilis_tarihi` (TEXT):** Dispatch date.
- **`tahmini_teslim` (TEXT):** Estimated delivery date.
- **`teslim_tarihi` (TEXT):** Actual delivery date.
- **`plaka` (TEXT):** Delivery truck's license plate.
- **`surucu` (TEXT):** Truck driver name.
- **`takip_notu` (TEXT):** Cargo tracking notes.

### B. `cargo_gecmis` (Cargo Update History)
Keeps snapshots of prior cargo records whenever an update occurs (historization tracking).
- **`id` (INTEGER - PRIMARY KEY AUTOINCREMENT):** Unique log entry ID.
- **`kargo_no` (TEXT - NOT NULL):** Cargo tracking number.
- **`gonderici` (TEXT - NOT NULL):** Historical sender.
- **`alici` (TEXT - NOT NULL):** Historical receiver.
- **`gonderici_sube` (TEXT):** Historical origin branch.
- **`teslimat_sube` (TEXT):** Historical destination branch.
- **`desi` (REAL):** Historical volumetric weight.
- **`agirlik` (REAL):** Historical weight.
- **`durum` (TEXT):** Historical status.
- **`verilis_tarihi` (TEXT):** Historical dispatch date.
- **`tahmini_teslim` (TEXT):** Historical estimated delivery.
- **`teslim_tarihi` (TEXT):** Historical actual delivery date.
- **`plaka` (TEXT):** Historical truck license plate.
- **`surucu` (TEXT):** Historical driver.
- **`takip_notu` (TEXT):** Historical tracking note.
- **`islem_turu` (TEXT):** Type of operation (e.g., "Güncellendi").
- **`degisiklik_tarihi` (TEXT):** Timestamp of when the modification took place.

### C. `urun` (Warehouse Stock / Inventory)
Manages inventory at warehouses.
- **`urun_kodu` (TEXT - PRIMARY KEY):** Unique product code.
- **`urun_adi` (TEXT - NOT NULL):** Product name.
- **`kategori` (TEXT - NOT NULL):** Product category.
- **`marka` (TEXT):** Brand name.
- **`tedarikci` (TEXT):** Supplier name.
- **`depo` (TEXT):** Storage warehouse identifier.
- **`raf_no` (TEXT):** Shelf number/aisle code.
- **`birim` (TEXT):** Unit of measurement (e.g., Adet, Kg, Koli).
- **`stok_miktari` (INTEGER - NOT NULL):** Current quantity in stock.
- **`kritik_limit` (INTEGER - NOT NULL):** Minimum threshold limit (if stock drops below this, UI highlights warning).
- **`giris_tarihi` (TEXT):** Warehouse entry date.

### D. `ulasim` (Transportation / Transit Routes)
Handles vehicle dispatching, transit, route details, and delivery validation.
- **`plaka` (TEXT - PRIMARY KEY):** License plate of vehicle.
- **`surucu` (TEXT - NOT NULL):** Driver's name.
- **`baslangic` (TEXT - NOT NULL):** Origin terminal/city.
- **`varis` (TEXT - NOT NULL):** Destination terminal/city.
- **`rota` (TEXT):** Transit route instructions.
- **`guncel_konum` (TEXT):** Current GPS location description.
- **`baslangic_zamani` (TEXT):** Departure timestamp.
- **`tahmini_sure` (INTEGER):** Estimated transit duration in hours.
- **`toplam_mesafe` (REAL):** Total route distance in km.
- **`yakit` (REAL):** Estimated/consumed fuel in liters.
- **`rotadan_cikti` (TEXT):** Indicator if the vehicle deviated from the route ("Evet"/"Hayır").
- **`teslim_alindi` (TEXT):** Loaded/Picked up status ("Evet"/"Hayır").
- **`koli_no` (TEXT):** Box/Package number loaded.
- **`teslim_alma_zamani` (TEXT):** Pickup timestamp.
- **`teslim_alan` (TEXT):** Name of person signing off pickup.
- **`teslim_alinan_firma` (TEXT):** Cargo pickup company.
- **`teslim_edildi` (TEXT):** Delivered status ("Evet"/"Hayır").
- **`teslim_zamani` (TEXT):** Delivery timestamp.
- **`musteri` (TEXT):** Receiver customer/firm name.
- **`onay_kodu` (TEXT):** Security confirmation PIN/Code for delivery validation.
- **`rota_durumu` (TEXT):** Active state of route (e.g., "Tamamlandı", "Devam Ediyor").
- **`aciklama` (TEXT):** Status details or exceptions description.

### E. `mesai` (Employee Overtime Log & Payroll Helper)
Calculates and tracks employee extra shifts and associated wage payouts.
- **`mesai_no` (TEXT - PRIMARY KEY):** Overtime record identifier.
- **`sicil_no` (TEXT - NOT NULL):** Employee register/ID number.
- **`ad_soyad` (TEXT - NOT NULL):** Employee full name.
- **`departman` (TEXT):** Department (e.g., "Operasyon", "Depo").
- **`pozisyon` (TEXT):** Job title (e.g., "Şoför", "Forklift Operatörü").
- **`mesai_tarihi` (TEXT):** Date of the extra shift.
- **`baslangic_saati` (TEXT):** Shift start hour.
- **`bitis_saati` (TEXT):** Shift end hour.
- **`mola_suresi` (INTEGER):** Break duration in minutes.
- **`toplam_mesai_saati` (REAL):** Net overtime duration (calculated as total shift hours minus break hours).
- **`mesai_nedeni` (TEXT):** Reason for overtime.
- **`yapilan_is` (TEXT):** Description of duties performed.
- **`proje_operasyon` (TEXT):** Associated project/customer campaign.
- **`sube_depo` (TEXT):** Branch or warehouse branch where shift took place.
- **`aciklama` (TEXT):** Additional comments.
- **`aylik_ucret` (REAL):** Employee's base monthly salary.
- **`aylik_calisma_saati` (INTEGER):** Monthly base contract hours (usually standard 180 or 225 hours).
- **`mesai_katsayisi` (REAL):** Overtime multiplier coefficient (e.g., 1.5x or 2.0x).
- **`normal_saatlik_ucret` (REAL):** Derived normal hourly rate (`aylik_ucret / aylik_calisma_saati`).
- **`mesai_saat_ucreti` (REAL):** Derived overtime hourly rate (`normal_saatlik_ucret * mesai_katsayisi`).
- **`toplam_mesai_ucreti` (REAL):** Calculated overtime salary payout (`toplam_mesai_saati * mesai_saat_ucreti`).
- **`rota_plani_var_mi` (TEXT):** Whether driver had a delivery route during shift ("Evet"/"Hayır").
- **`sirali_teslimat` (TEXT):** Sequence delivery indicator.
- **`teslimat_sorunu` (TEXT):** Delivery obstacles/issues description.
- **`yerinde_kapatma` (TEXT):** On-site closing indicator.
- **`irsaliye_atf_no` (TEXT):** Waybill/ATF document reference number.
- **`yonetici` (TEXT):** Manager in charge of approval.
- **`onay_durumu` (TEXT):** Approval status (e.g., "Bekliyor", "Onaylandı", "Reddedildi").
- **`onay_tarihi` (TEXT):** Manager approval timestamp.
- **`odeme_durumu` (TEXT):** Payment status (e.g., "Ödenmedi", "Ödendi").
- **`odeme_tarihi` (TEXT):** Date of payout.

---

## 4. API Endpoints

The server exposes standard REST capabilities for the frontend:

| Endpoint | Method | Query Parameters | Description |
| :--- | :--- | :--- | :--- |
| `/` | `GET` | *None* | Serves the main homepage/dashboard. |
| `/kargo` | `GET` | *None* | Serves the cargo management HTML page. |
| `/kargo-detay` | `GET` | `kargoNo` | Serves the cargo detail page. |
| `/stok` | `GET` | *None* | Serves the stock control HTML page. |
| `/ulasim` | `GET` | *None* | Serves the transit tracking HTML page. |
| `/mesai` | `GET` | *None* | Serves the overtime log HTML page. |
| `/api/kargo` | `GET` | `kargoNo` *(optional)* | If `kargoNo` is provided, returns that specific cargo JSON. If empty, returns all cargo logs. |
| `/api/kargo` | `POST` | *None* | Saves a new cargo record. Request body must be a Cargo JSON. |
| `/api/kargo` | `PUT` | *None* | Updates cargo details. Request body must be a Cargo JSON. |
| `/api/kargo` | `DELETE` | `kargoNo` | Deletes a cargo record by tracking number. |
| `/api/kargo-gecmis`| `GET` | `kargoNo` *(required)* | Retrieves history timeline logs (`cargo_gecmis` entries) for the specified cargo number. |
| `/api/urun` | `GET` | `urunKodu` *(optional)* | If `urunKodu` is provided, returns that product JSON. If empty, returns all products. |
| `/api/urun` | `POST` | *None* | Creates a new product stock. Request body must be an Urun JSON. |
| `/api/urun` | `PUT` | *None* | Updates product details. Request body must be an Urun JSON. |
| `/api/urun` | `DELETE` | `urunKodu` | Deletes product stock by product code. |
| `/api/ulasim` | `GET` | `plaka` *(optional)* | If `plaka` is provided, returns transit info for that license plate. If empty, returns all routes. |
| `/api/ulasim` | `POST` | *None* | Dispatches/records a new transit route. Request body must be an Ulasim JSON. |
| `/api/ulasim` | `PUT` | *None* | Updates vehicle route tracking. Request body must be an Ulasim JSON. |
| `/api/ulasim` | `DELETE` | `plaka` | Deletes transit record by license plate. |
| `/api/mesai` | `GET` | `mesaiNo` *(optional)* | If `mesaiNo` is provided, returns that overtime record. If empty, returns all logs. |
| `/api/mesai` | `POST` | *None* | Creates a new overtime log. Request body must be a Mesai JSON. |
| `/api/mesai` | `PUT` | *None* | Updates overtime record. Request body must be a Mesai JSON. |
| `/api/mesai` | `DELETE` | `mesaiNo` | Deletes overtime record by overtime ID. |

---

## 5. Key Business Logic Implementations

### Cargo Historization (Automatic Triggers)
The system does not use SQLite database triggers. Instead, the update-historization logic is implemented in `CargoDAO.java` inside the `update` method:
1. When an update is requested for a cargo item, the DAO first fetches the existing record (`eskiCargo`) using `findByKargoNo`.
2. If the cargo exists, it calls `CargoGecmisDAO.gecmiseKaydet(eskiCargo)`.
3. This duplicates the old record into the `cargo_gecmis` table, setting `islem_turu` to `"Güncellendi"` and registering the current system timestamp.
4. Only then does the DAO execute the SQL `UPDATE` statement on the primary `cargo` table.

### Overtime Financial Calculations
Wage and payment math is tracked in the `Mesai` object. The fields are calculated and filled either on the frontend side before sending the JSON body or handled in the application flow:
- **Normal Hourly Rate (`normal_saatlik_ucret`):** `aylik_ucret / aylik_calisma_saati`
- **Overtime Hourly Rate (`mesai_saat_ucreti`):** `normal_saatlik_ucret * mesai_katsayisi` (multiplied by coefficient, typically `1.5` or `2.0`)
- **Total Overtime Wage (`toplam_mesai_ucreti`):** `toplam_mesai_saati * mesai_saat_ucreti`

---

## 6. How to Build and Run the Project

### Prerequisites
1. **Java Development Kit (JDK):** Version 23 installed and configured.
2. **Apache Maven:** Installed and configured in the path.

### Compilation
Build the application utilizing Maven from the project root:
```bash
mvn clean compile
```

### Running the Server
Execute the main method in `Main.java` to spin up the HTTP Server:
```bash
mvn exec:java -Dexec.mainClass="Main"
```
The server will output:
```text
Cargo tablosu hazır.
Ürün tablosu hazır.
Ulaşım tablosu hazır.
Mesai tablosu hazır.
Cargo Geçmiş tablosu hazır.
Toplam kargo sayısı: X
Toplam ürün sayısı: Y
Toplam ulaşım kaydı: Z
Toplam mesai kaydı: W
SQLite bağlantısı başarılı.
Sunucu çalışıyor: http://localhost:8080
```
Open a browser and navigate to `http://localhost:8080` to access the Horoz Lojistik Operations Management Panel.

---

## 7. System Flaws, Optimizations, and Improvements

During analysis of the codebase, several design flaws, performance bottlenecks, and structural weaknesses were identified. Below is a detailed review of these issues alongside recommended optimizations and improvements.

### A. Critical Security & Logical Flaws

1. **No Transaction Management (Atomicity Violation)**
   - **Flaw:** In `CargoDAO.update(Cargo cargo)`, if the cargo exists, its history is saved using `cargoGecmisDAO.gecmiseKaydet(eskiCargo)` first. After that, the main `cargo` table is updated. If the main database update fails (e.g., connection drop, database locked, or constraint violation), the history has already been written. There is no rollback mechanism.
   - **Fix:** Disable auto-commit (`connection.setAutoCommit(false)`), execute both queries in the same connection context, and perform a rollback on `SQLException` if either statement fails, only committing on complete success.

2. **Client-Side Financial Calculation Trust (Security Risk)**
   - **Flaw:** The calculations for overtime wages (`toplam_mesai_ucreti`, `normal_saatlik_ucret`, `mesai_saat_ucreti`) are generated on the frontend and sent directly to `/api/mesai`. The backend saves these values without checking or recalculating them. A malicious user could edit the request payload to artificially inflate their overtime payments.
   - **Fix:** Shift wage arithmetic to the backend. The API should accept only the raw shift dates, times, breaks, and base salary. The Java backend should calculate the hourly rates and total wages before saving them to the database.

3. **Lack of Authentication & Authorization**
   - **Flaw:** The system has no login/session verification. All CRUD endpoints (`GET`, `POST`, `PUT`, `DELETE` under `/api/`) are completely public. Anyone who can reach the server port can delete inventory, approve payments, or manipulate transit routes.
   - **Fix:** Introduce JWT or session-based cookies. Restrict sensitive routes (like cargo deletion, mesai approvals, and stock updates) to authorized role levels (e.g., Managers, Admin).

4. **Lack of Input Validation**
   - **Flaw:** Data models accept arbitrary values. For example, product quantities (`stok_miktari`), weights (`agirlik`), base pay (`aylik_ucret`), and multiplier coefficients can be set to negative numbers.
   - **Fix:** Implement validator classes/utility helpers. Ensure weights, salary, and quantities are non-negative and dates match expected format strings.

---

### B. Database & Performance Flaws

1. **Direct JDBC Connection Overhead**
   - **Flaw:** Every DAO query invokes `DriverManager.getConnection()`. This physically opens, reads, and closes a connection to `cargo.db` on every single request. Under heavy load, this will degrade performance due to excessive I/O overhead.
   - **Fix:** Integrate a lightweight Connection Pooling library such as **HikariCP** or reuse a single static SQLite connection, as SQLite is a local file-based database.

2. **Concurrency & Thread Locking (`SQLITE_BUSY` Risk)**
   - **Flaw:** Java's `HttpServer` processes incoming web requests concurrently on multiple threads. Since SQLite does not allow concurrent writes on the same database file without synchronization, simultaneous POST/PUT requests are prone to throwing `SQLITE_BUSY` exceptions.
   - **Fix:** Synchronize DAO write operations or use a SQLite configuration that enables WAL (Write-Ahead Logging) mode alongside an appropriate connection busy-timeout threshold.

3. **Inconsistent Date/Time Formats**
   - **Flaw:** Timestamps (like `degisiklik_tarihi`, `tahmini_teslim`, and `verilis_tarihi`) are saved as text strings (`TEXT`) without format enforcement. Since there is no validation, date formats can mismatch, making range queries (`BETWEEN` date boundaries) impossible or extremely buggy.
   - **Fix:** Standardize date columns. Store dates as Unix epoch timestamps (integers) or enforce ISO-8601 strings (e.g., `YYYY-MM-DDTHH:MM:SSZ`) at both database and model levels.

4. **Hardcoded Database & Server Properties**
   - **Flaw:** The port `8080` and the database path `jdbc:sqlite:cargo.db` are hardcoded in `Main.java` and `Database.java`. Deploying the server to a different port or path requires editing source code and rebuilding.
   - **Fix:** Read configuration parameters from environment variables (e.g., `System.getenv("PORT")`) or a `config.properties` file in `src/main/resources`.

---

### C. Recommended Optimizations & Improvements

1. **Adopt a REST Framework**
   - **Improvement:** Replace the low-level `com.sun.net.httpserver.HttpServer` with a microframework like **Javalin**, **SparkJava**, or **Spring Boot**. This would eliminate boilerplate code for parsing query parameters, manual resource streaming, JSON formatting, and exception handling.

2. **Introduce ORM (Object-Relational Mapping)**
   - **Improvement:** Use **MyBatis** or **Hibernate/JPA** to replace raw SQL strings and manual `ResultSet` mappings in DAO classes. This significantly reduces database boilerplate and ensures safer schema evolution.

3. **Global Exception Mapping & Standard API Responses**
   - **Improvement:** Instead of returning plain text for database errors and dumping stack traces to the stdout using `e.printStackTrace()`, implement a global exception handling wrapper. Ensure the client receives structured JSON error responses containing appropriate HTTP status codes (e.g., `{"status": 400, "message": "Product code already exists"}`).
