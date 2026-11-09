# AFSIN – MATH WHIZ
### Web-Based Mathematical Challenge Game using Java Servlet & JDBC
**Practical Lab Assignment | Beginner–Intermediate | 10 Marks**

[![Java](https://img.shields.io/badge/Java-17%2F21%2B-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Servlet](https://img.shields.io/badge/Servlet-4.0%2F3.1-007396?logo=apachetomcat&logoColor=white)](https://tomcat.apache.org/)
[![Database](https://img.shields.io/badge/MySQL-8.0%2F8.4-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

## 01. Project Title & Description

**AFSIN – MATH WHIZ** is a server-side web application developed in Java using standard Java Servlets, JavaServer Pages (JSP), and JDBC (Java Database Connectivity). The application delivers an interactive mathematical challenge that allows players to input their own test numbers and calculate answers for three core quantitative areas:

1. **Prime Number Validation** (determine whether a given integer is prime)
2. **Modulus Arithmetic** (calculate division remainder with zero-divisor validation)
3. **Trigonometric Functions** (calculate Sine, Cosine, or Tangent from degree inputs converted to radians)

All calculations are evaluated **dynamically using reusable Java methods** (`MathService.java`) rather than static or pre-recorded answers. Player scores, difficulty levels (`NORMAL`), and timestamps are recorded in a MySQL database (`math_whiz_db`), and a real-time **Top 10 Leaderboard** ranks champions based on score and time.

---

## 02. Project Objectives

- Implement reusable, modular Java methods for mathematical computations.
- Master server-side request/response processing using `HttpServlet` (`GameServlet`).
- Design and execute secure SQL operations using JDBC and `PreparedStatement`.
- Build an interactive, user-friendly multi-screen interface with JSP and CSS.
- Manage persistent state and leaderboard ranking with sorting criteria.

---

## 03. Technologies & Software Requirements

### Technologies Used
| Layer | Technology |
|---|---|
| **Language** | Java (JDK 17 / 21+) |
| **Server-Side Engine** | Java Servlet 4.0 / 3.1 (`javax.servlet`) |
| **View Presentation** | JavaServer Pages (JSP) + Custom Responsive CSS |
| **Database** | MySQL Server 8.0+ / MariaDB (XAMPP compatible) |
| **Database Connectivity**| JDBC (`mysql-connector-j-8.4.0.jar`) with `PreparedStatement` |
| **Testing** | JUnit 4 & Standalone Console Verification Runner (`MathWhizTester`) |
| **Build & Packaging** | Apache Maven / Standard WAR archive |

### Software Requirements
- **Java Development Kit (JDK):** Version 17 or higher.
- **Web / Application Server:** Apache Tomcat 8.5, 9.0, or 10.1 (or standalone runner).
- **Database Server:** MySQL Server (e.g., via XAMPP Control Panel).
- **Web Browser:** Google Chrome, Mozilla Firefox, or Microsoft Edge.

---

## 04. Game Rules & Scoring System

The game presents **4 challenges** worth a maximum of **10 Marks**:

| Question # | Mathematical Area | Evaluation Criteria | Marks |
|---|---|---|---|
| **Q1** | **Prime Number** | Return `1` if the number is prime; otherwise return `0`. Numbers less than 2 are not prime. | **2 Marks** |
| **Q2** | **Modulus Operation** | Calculate integer remainder `a % b`. Divisor `b` cannot be 0 (handled via `ArithmeticException`). | **2 Marks** |
| **Q3** | **Trigonometric Value 1** | Calculate `sin(θ)`, `cos(θ)`, or `tan(θ)` using `Math.toRadians(deg)`. Floating-point tolerance of $\pm 0.05$. | **3 Marks** |
| **Q4** | **Trigonometric Value 2** | Calculate `sin(θ)`, `cos(θ)`, or `tan(θ)` using `Math.toRadians(deg)`. Floating-point tolerance of $\pm 0.05$. | **3 Marks** |
| **TOTAL** | — | — | **10 Marks** |

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
* **NORMAL** (Recorded in database for all games as per specification).

---

## 05. Database Configuration & Schema

### Database Details
- **Database Name:** `math_whiz_db`
- **Table Name:** `game_scores`
- **Default Port:** `3306`
- **Default Username:** `root`
- **Default Password:** `""` (blank, standard for XAMPP)

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

---

## 06. Steps to Run the Application

### Option A: 1-Click Instant Launch (Recommended)
1. Start **MySQL** in **XAMPP Control Panel**.
2. Double-click `Play_MathWhiz.bat` in the project root.
3. Your browser will automatically open: `http://localhost:8080/`

---

### Option B: Deploying WAR to Apache Tomcat (XAMPP)
1. Copy `target/MathWhiz.war` to your Tomcat webapps directory:
   `C:\xampp\tomcat\webapps\MathWhiz.war`
2. Start Tomcat in XAMPP Control Panel.
3. Open browser and visit: `http://localhost:8080/MathWhiz/`

---

### Option C: Interactive Command-Line Console Mode
```bash
java -cp "target\classes;target\MathWhiz\WEB-INF\lib\*" util.ConsoleGame
```

---

## 07. Application Screenshots

### Welcome Page
*Screen 1: Player name input with Afsin auto-suggestion and game rules overview.*
![Welcome Page](screenshots/home.png)

### Game Page
*Screen 2: Four mathematical challenges with user-entered numbers and answers.*
![Game Page](screenshots/game.png)

### Result Page
*Screen 3: Final score (out of 10), performance badge, and detailed question breakdown.*
![Result Page](screenshots/result.png)

### Leaderboard
*Screen 4: Top 10 Hall of Fame ranked by highest score and completion time.*
![Leaderboard](screenshots/leaderboard.png)

---

## 08. GitHub Submission Checklist

- [x] Complete Java source code (`GameServlet`, `GameScore`, `MathService`, `ScoreDAO`, `DBConnection`).
- [x] Servlet & JSP implementation with dynamic server-side processing.
- [x] JDBC database connectivity with `PreparedStatement`.
- [x] Database SQL script (`database/math_whiz_db.sql`).
- [x] Four-question mathematical game covering Prime, Modulus, and Trigonometry.
- [x] Score calculation adhering to the 10-mark distribution.
- [x] Top 10 leaderboard sorted by `score DESC, played_at ASC`.
- [x] Complete README.md documentation with setup steps.
- [x] Application screenshots (`screenshots/*.png`).
- [x] Ready for GitHub submission.

### GitHub Repository Link
```text
https://github.com/it24054-cyber/Afsin-Math-Whiz
```

---
*Created by Afsin for Practical Lab Assignment: Java Web Application Development.*
