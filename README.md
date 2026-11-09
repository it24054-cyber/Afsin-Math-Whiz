<div align="center">

# 🧮 AFSIN – MATH WHIZ

### A Web-Based Mathematical Challenge Game built with Java Servlet, JSP & JDBC

**Practical Lab Assignment · Java Web Application Development · 10 Marks**

[![Java](https://img.shields.io/badge/Java-17%2F21%2B-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Servlet](https://img.shields.io/badge/Servlet-4.0%2F3.1-007396?logo=apachetomcat&logoColor=white)](https://tomcat.apache.org/)
[![JSP](https://img.shields.io/badge/View-JSP%20%2B%20CSS-orange)](#04-system-architecture)
[![Database](https://img.shields.io/badge/MySQL-8.0%2F8.4-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Tests](https://img.shields.io/badge/Tests-JUnit%204-25A162)](#11-testing)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

</div>

---

## 📑 Table of Contents

1. [Project Title & Description](#01-project-title--description)
2. [Project Objectives](#02-project-objectives)
3. [Features at a Glance](#03-features-at-a-glance)
4. [System Architecture](#04-system-architecture)
5. [Game Rules & Scoring System](#05-game-rules--scoring-system)
6. [How Each Question Is Evaluated](#06-how-each-question-is-evaluated)
7. [Database Design](#07-database-design)
8. [Technologies & Software Requirements](#08-technologies--software-requirements)
9. [Project Structure](#09-project-structure)
10. [Steps to Run the Application](#10-steps-to-run-the-application)
11. [Testing](#11-testing)
12. [Application Screenshots](#12-application-screenshots)
13. [Troubleshooting](#13-troubleshooting)
14. [Learning Outcomes & Future Improvements](#14-learning-outcomes--future-improvements)
15. [GitHub Submission Checklist](#15-github-submission-checklist)

---

## 01. Project Title & Description

**AFSIN – MATH WHIZ** is a server-side web application developed in Java using standard **Java Servlets**, **JavaServer Pages (JSP)** and **JDBC (Java Database Connectivity)**. The player enters their own test numbers and the server calculates the answers for three core quantitative areas:

| # | Area | What the player does |
|---|------|----------------------|
| 1 | **Prime Number Validation** | Checks whether a given integer is prime |
| 2 | **Modulus Arithmetic** | Calculates a division remainder, with zero-divisor validation |
| 3 | **Trigonometric Functions** | Calculates Sine, Cosine or Tangent from a degree input converted to radians |

All answers are evaluated **dynamically by reusable Java methods** (`MathService.java`), never by static or pre-recorded values. Player scores, the difficulty level (`NORMAL`) and timestamps are stored in a MySQL database (`math_whiz_db`), and a live **Top 10 Leaderboard** ranks players by score and completion time.

---

## 02. Project Objectives

- Implement **reusable, modular** Java methods for mathematical computations.
- Master **server-side request/response processing** using `HttpServlet` (`GameServlet`).
- Design and execute **secure SQL operations** using JDBC and `PreparedStatement`.
- Build an interactive, user-friendly **multi-screen interface** with JSP and CSS.
- Manage **persistent state and leaderboard ranking** with proper sorting criteria.

---

## 03. Features at a Glance

```mermaid
mindmap
  root((AFSIN<br/>MATH WHIZ))
    Challenges
      Prime Number
      Modulus
      Trigonometry x2
    Server Side
      HttpServlet
      JSP Views
      MathService
    Database
      JDBC
      PreparedStatement
      MySQL
    Player Experience
      Name Input
      Instant Score
      Performance Badge
      Top 10 Leaderboard
    Quality
      JUnit Tests
      Console Runner
      Input Validation
```

---

## 04. System Architecture

The application follows a clean **MVC-style, layered design**. JSP pages are the *View*, `GameServlet` is the *Controller*, and `MathService`, `GameScore`, `ScoreDAO` and `DBConnection` form the *Model / data layer*.

### 4.1 Layered Architecture

```mermaid
flowchart TB
    subgraph Client["🌐 Client Layer"]
        B["Web Browser<br/>(Chrome / Firefox / Edge)"]
    end

    subgraph Web["🖥️ Presentation Layer (Apache Tomcat)"]
        V1["Welcome Page (JSP)"]
        V2["Game Page (JSP)"]
        V3["Result Page (JSP)"]
        V4["Leaderboard Page (JSP)"]
    end

    subgraph Ctrl["🎮 Controller Layer"]
        S["GameServlet<br/>(HttpServlet)"]
    end

    subgraph Biz["⚙️ Business Logic Layer"]
        M["MathService<br/>(Prime · Modulus · Trig)"]
        G["GameScore<br/>(Model / POJO)"]
    end

    subgraph Data["🗄️ Data Access Layer"]
        D["ScoreDAO<br/>(PreparedStatement)"]
        C["DBConnection<br/>(JDBC)"]
    end

    DB[("MySQL<br/>math_whiz_db")]

    B <-->|HTTP Request / Response| S
    S --> V1 & V2 & V3 & V4
    S --> M
    S --> G
    S --> D
    D --> C --> DB
```

### 4.2 End-to-End Request Flow (Sequence Diagram)

```mermaid
sequenceDiagram
    autonumber
    actor P as Player
    participant B as Browser
    participant S as GameServlet
    participant M as MathService
    participant D as ScoreDAO
    participant DB as MySQL (math_whiz_db)

    P->>B: Enter name and start game
    B->>S: GET / (open welcome page)
    S-->>B: Render Welcome JSP
    P->>B: Submit own numbers and answers
    B->>S: POST /game (form data)
    S->>M: Compute correct answers (prime, mod, trig)
    M-->>S: Correct values
    S->>S: Compare answers and calculate score (0 to 10)
    S->>D: saveScore(name, score, "NORMAL")
    D->>DB: INSERT INTO game_scores (PreparedStatement)
    DB-->>D: Row saved
    S-->>B: Render Result JSP (score + breakdown)
    P->>B: Click "Leaderboard"
    B->>S: GET /leaderboard
    S->>D: getTop10()
    D->>DB: SELECT ... ORDER BY score DESC, played_at ASC LIMIT 10
    DB-->>D: 10 rows
    S-->>B: Render Leaderboard JSP
```

### 4.3 Screen Navigation (State Diagram)

```mermaid
stateDiagram-v2
    [*] --> Welcome
    Welcome --> Game: Enter name and Start
    Game --> Result: Submit answers
    Result --> Game: Play Again
    Result --> Leaderboard: View Top 10
    Leaderboard --> Welcome: Home
    Leaderboard --> Game: Play Again
    Welcome --> [*]: Close browser
```

### 4.4 Class Diagram (simplified)

> Method names are simplified for illustration; see the source files for exact signatures.

```mermaid
classDiagram
    class GameServlet {
        +doGet(request, response)
        +doPost(request, response)
        -calculateScore() int
    }
    class MathService {
        +prime(n) int
        +modulus(a, b) int
        +trig(function, degrees) double
    }
    class GameScore {
        -int id
        -String playerName
        -int score
        -String difficulty
        -Timestamp playedAt
    }
    class ScoreDAO {
        +saveScore(GameScore)
        +getTop10() List~GameScore~
    }
    class DBConnection {
        +getConnection() Connection
    }

    GameServlet --> MathService : uses
    GameServlet --> ScoreDAO : uses
    GameServlet --> GameScore : creates
    ScoreDAO --> DBConnection : opens
    ScoreDAO ..> GameScore : maps rows
```

---

## 05. Game Rules & Scoring System

The game presents **4 challenges** worth a maximum of **10 Marks**:

| Question # | Mathematical Area | Evaluation Criteria | Marks |
|---|---|---|---|
| **Q1** | **Prime Number** | Return `1` if the number is prime, otherwise `0`. Numbers less than 2 are not prime. | **2** |
| **Q2** | **Modulus Operation** | Calculate integer remainder `a % b`. Divisor `b` cannot be 0 (handled via `ArithmeticException`). | **2** |
| **Q3** | **Trigonometric Value 1** | Calculate `sin(θ)`, `cos(θ)` or `tan(θ)` using `Math.toRadians(deg)`. Tolerance ±0.05. | **3** |
| **Q4** | **Trigonometric Value 2** | Calculate `sin(θ)`, `cos(θ)` or `tan(θ)` using `Math.toRadians(deg)`. Tolerance ±0.05. | **3** |
| **TOTAL** | — | — | **10** |

### Mark Distribution

```mermaid
pie showData title Marks per Question (Total = 10)
    "Q1 Prime Number" : 2
    "Q2 Modulus" : 2
    "Q3 Trigonometry 1" : 3
    "Q4 Trigonometry 2" : 3
```

### Scoring Algorithm (`GameServlet.java`)

```java
int totalScore = 0;
if (primeCorrect) totalScore += 2;
if (modCorrect)   totalScore += 2;
if (trig1Correct) totalScore += 3;
if (trig2Correct) totalScore += 3;
// Maximum Possible: 10 Marks
```

### Difficulty Level

- **NORMAL**: recorded in the database for every game, as required by the specification.

---

## 06. How Each Question Is Evaluated

### 6.1 Prime Number (Q1)

```mermaid
flowchart LR
    A([Input integer n]) --> B{n < 2 ?}
    B -- Yes --> N[Return 0<br/>Not prime]
    B -- No --> C{Divisible by any<br/>number between<br/>2 and n-1 ?}
    C -- Yes --> N
    C -- No --> Y[Return 1<br/>Prime]
    Y --> E([Compare with player answer])
    N --> E
```

### 6.2 Modulus (Q2)

```mermaid
flowchart LR
    A([Input a and b]) --> B{b == 0 ?}
    B -- Yes --> X[Throw ArithmeticException<br/>Show friendly error message]
    B -- No --> R["Compute a % b"]
    R --> E([Compare with player answer])
```

### 6.3 Trigonometry (Q3 and Q4)

```mermaid
flowchart LR
    A([Input degrees and function]) --> B["Convert: Math.toRadians(deg)"]
    B --> C{Function?}
    C -- sin --> S["Math.sin(rad)"]
    C -- cos --> CO["Math.cos(rad)"]
    C -- tan --> T["Math.tan(rad)"]
    S --> D{"abs(correct - answer)<br/>≤ 0.05 ?"}
    CO --> D
    T --> D
    D -- Yes --> OK[Correct: +3 marks]
    D -- No --> NO[Wrong: +0 marks]
```

### 6.4 Sample Test Values

| Question | Input | Expected result |
|---|---|---|
| Prime | `17` | `1` (prime) |
| Prime | `18` | `0` (not prime) |
| Prime | `1` | `0` (less than 2) |
| Modulus | `17 % 5` | `2` |
| Modulus | `10 % 0` | Error: division by zero handled |
| Trig | `sin(30°)` | `0.5` |
| Trig | `cos(60°)` | `0.5` |
| Trig | `tan(45°)` | `1.0` |

---

## 07. Database Design

### Database Details

| Property | Value |
|---|---|
| **Database Name** | `math_whiz_db` |
| **Table Name** | `game_scores` |
| **Default Port** | `3306` |
| **Default Username** | `root` |
| **Default Password** | `""` (blank, standard for XAMPP) |

### Entity-Relationship Diagram

```mermaid
erDiagram
    GAME_SCORES {
        INT id PK "AUTO_INCREMENT"
        VARCHAR player_name "NOT NULL, max 100"
        INT score "NOT NULL, 0 to 10"
        VARCHAR difficulty "DEFAULT NORMAL"
        TIMESTAMP played_at "DEFAULT CURRENT_TIMESTAMP"
    }
```

### Leaderboard Ranking Logic

```mermaid
flowchart LR
    A[(game_scores)] --> B["ORDER BY<br/>score DESC"]
    B --> C["then<br/>played_at ASC<br/>(earlier wins ties)"]
    C --> D["LIMIT 10"]
    D --> E([🏆 Top 10 Leaderboard])
```

### Database Creation SQL (`database/math_whiz_db.sql`)

```sql
-- 1. Create Database
CREATE DATABASE IF NOT EXISTS math_whiz_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE math_whiz_db;

-- 2. Create game_scores Table
CREATE TABLE IF NOT EXISTS game_scores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    player_name VARCHAR(100) NOT NULL,
    score INT NOT NULL,
    difficulty VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Top 10 Leaderboard Query (Task 08)
SELECT id, player_name, score, difficulty, played_at
FROM game_scores
ORDER BY score DESC, played_at ASC
LIMIT 10;
```

> 🔒 **Security note:** all database operations use `PreparedStatement`, which protects the application against **SQL injection**.

---

## 08. Technologies & Software Requirements

### Technologies Used

| Layer | Technology |
|---|---|
| **Language** | Java (JDK 17 / 21+) |
| **Server-Side Engine** | Java Servlet 4.0 / 3.1 (`javax.servlet`) |
| **View Presentation** | JavaServer Pages (JSP) + Custom Responsive CSS |
| **Database** | MySQL Server 8.0+ / MariaDB (XAMPP compatible) |
| **Database Connectivity** | JDBC (`mysql-connector-j-8.4.0.jar`) with `PreparedStatement` |
| **Testing** | JUnit 4 and Standalone Console Verification Runner (`MathWhizTester`) |
| **Build & Packaging** | Apache Maven / Standard WAR archive |

### Software Requirements

- **Java Development Kit (JDK):** version 17 or higher.
- **Web / Application Server:** Apache Tomcat 8.5, 9.0 or 10.1 (or standalone runner).
- **Database Server:** MySQL Server (e.g., via XAMPP Control Panel).
- **Web Browser:** Google Chrome, Mozilla Firefox or Microsoft Edge.

> ⚠️ **Compatibility note:** the code uses the `javax.servlet` namespace, which works on **Tomcat 8.5 and 9.0**. Tomcat 10+ switched to `jakarta.servlet`, so on Tomcat 10.1 the imports must be migrated to `jakarta.servlet.*` (or use the bundled 1-click launcher, which runs a compatible server).

### Deployment View

```mermaid
flowchart LR
    subgraph PC["💻 Developer / Lab PC"]
        subgraph X["XAMPP"]
            T["Apache Tomcat<br/>:8080<br/>MathWhiz.war"]
            M[("MySQL<br/>:3306<br/>math_whiz_db")]
        end
        BR["Web Browser"]
    end
    BR -->|"http://localhost:8080/MathWhiz/"| T
    T -->|"JDBC (mysql-connector-j)"| M
```

---

## 09. Project Structure

```text
MathWhiz/
├── src/
│   ├── controller/    GameServlet.java
│   ├── model/         GameScore.java
│   ├── service/       MathService.java
│   ├── dao/           ScoreDAO.java
│   ├── util/          DBConnection.java, ConsoleGame.java
│   └── test/          MathWhizTester.java, JUnit tests
├── webapp/
│   ├── *.jsp          Welcome, Game, Result, Leaderboard views
│   ├── css/           Responsive stylesheet
│   └── WEB-INF/       web.xml, lib/ (JDBC driver)
├── database/
│   └── math_whiz_db.sql
├── screenshots/       home.png, game.png, result.png, leaderboard.png
├── target/            MathWhiz.war (build output)
├── Play_MathWhiz.bat  1-click launcher
├── pom.xml
└── README.md
```

> The tree is a guide; adjust folder names to match your actual repository.

---

## 10. Steps to Run the Application

```mermaid
flowchart TD
    S([Start]) --> Q{Choose a mode}
    Q -->|A: Fastest| A1["Start MySQL in XAMPP"]
    A1 --> A2["Double-click Play_MathWhiz.bat"]
    A2 --> A3["Browser opens<br/>localhost:8080"]
    Q -->|B: Tomcat| B1["Copy MathWhiz.war to<br/>xampp/tomcat/webapps"]
    B1 --> B2["Start Tomcat + MySQL"]
    B2 --> B3["Open<br/>localhost:8080/MathWhiz/"]
    Q -->|C: Console| C1["Run util.ConsoleGame<br/>with java -cp"]
    A3 --> P([Play the game 🎮])
    B3 --> P
    C1 --> P
```

### Option A: 1-Click Instant Launch (Recommended)

1. Start **MySQL** in the **XAMPP Control Panel**.
2. Double-click `Play_MathWhiz.bat` in the project root.
3. Your browser opens automatically at `http://localhost:8080/`

---

### Option B: Deploying the WAR to Apache Tomcat (XAMPP)

1. Copy `target/MathWhiz.war` to your Tomcat webapps directory:
   `C:\xampp\tomcat\webapps\MathWhiz.war`
2. Start Tomcat in the XAMPP Control Panel.
3. Open the browser and visit: `http://localhost:8080/MathWhiz/`

---

### Option C: Interactive Command-Line Console Mode

```bash
java -cp "target\classes;target\MathWhiz\WEB-INF\lib\*" util.ConsoleGame
```

---

## 11. Testing

Testing is done at two levels: automated unit tests and a standalone console runner.

```mermaid
flowchart LR
    T1["JUnit 4<br/>Unit Tests"] --> R1["MathService<br/>prime / modulus / trig"]
    T2["MathWhizTester<br/>Console Runner"] --> R2["Prints PASS / FAIL<br/>for every case"]
    T3["Manual UI Testing"] --> R3["Welcome → Game →<br/>Result → Leaderboard"]
    R1 --> OK([✅ All checks pass])
    R2 --> OK
    R3 --> OK
```

| Test Area | Cases Covered |
|---|---|
| Prime | prime, composite, `0`, `1`, negative numbers |
| Modulus | normal values, divisor `0` (exception handled) |
| Trigonometry | `sin`, `cos`, `tan` with the ±0.05 tolerance |
| Scoring | all correct (10), all wrong (0), mixed answers |
| Database | insert score, Top 10 ordering, tie-break by time |

---

## 12. Application Screenshots

### Welcome Page
*Screen 1: Player name input with Afsin auto-suggestion and a game rules overview.*
![Welcome Page](screenshots/home.png)

### Game Page
*Screen 2: Four mathematical challenges with user-entered numbers and answers.*
![Game Page](screenshots/game.png)

### Result Page
*Screen 3: Final score (out of 10), performance badge and detailed question breakdown.*
![Result Page](screenshots/result.png)

### Leaderboard
*Screen 4: Top 10 Hall of Fame ranked by highest score and completion time.*
![Leaderboard](screenshots/leaderboard.png)

---

## 13. Troubleshooting

| Problem | Likely Cause | Fix |
|---|---|---|
| Blank page / HTTP 404 | WAR not deployed or wrong URL | Check `webapps/MathWhiz.war` and open `/MathWhiz/` |
| `Communications link failure` | MySQL is not running | Start MySQL in the XAMPP Control Panel |
| `No suitable driver found` | JDBC jar missing | Confirm `mysql-connector-j-8.4.0.jar` is in `WEB-INF/lib` |
| `Access denied for user 'root'` | Different MySQL password | Update the credentials in `DBConnection.java` |
| `Unknown database 'math_whiz_db'` | SQL script not executed | Run `database/math_whiz_db.sql` |
| Port `8080` already in use | Another server is using it | Stop that service or change the Tomcat port |
| `ClassNotFoundException: javax.servlet...` on Tomcat 10 | Namespace changed to `jakarta` | Use Tomcat 9.0 or migrate imports |

---

## 14. Learning Outcomes & Future Improvements

### What This Project Demonstrates

- Servlet lifecycle and `doGet` / `doPost` request handling.
- Separation of concerns using Controller, Service, DAO and View layers.
- Safe database access with JDBC and `PreparedStatement`.
- Exception handling for invalid input (for example, division by zero).
- Writing verifiable code with JUnit and a console test runner.

### Possible Future Improvements

```mermaid
timeline
    title Roadmap
    Current : 4 challenges
            : Top 10 leaderboard
            : NORMAL difficulty
    Next    : EASY and HARD levels
            : Randomly generated questions
            : Timer per game
    Later   : User login and profiles
            : REST API with JSON
            : Cloud deployment
```

---

## 15. GitHub Submission Checklist

- [x] Complete Java source code (`GameServlet`, `GameScore`, `MathService`, `ScoreDAO`, `DBConnection`).
- [x] Servlet and JSP implementation with dynamic server-side processing.
- [x] JDBC database connectivity with `PreparedStatement`.
- [x] Database SQL script (`database/math_whiz_db.sql`).
- [x] Four-question mathematical game covering Prime, Modulus and Trigonometry.
- [x] Score calculation adhering to the 10-mark distribution.
- [x] Top 10 leaderboard sorted by `score DESC, played_at ASC`.
- [x] Complete README.md documentation with setup steps and diagrams.
- [x] Application screenshots (`screenshots/*.png`).
- [x] Ready for GitHub submission.

### GitHub Repository Link

```text
https://github.com/it24054-cyber/Afsin-Math-Whiz
```

---

<div align="center">

*Created by **Afsin** for Practical Lab Assignment: Java Web Application Development.*

⭐ If you like this project, give it a star on GitHub!

</div>
