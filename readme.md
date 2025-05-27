# Financy
Online finance tracker app.

## Project Overview
Financy is a full-stack web application for tracking personal finances. It allows users to:
- Register and login to their accounts
- Add income and expense transactions
- Categorize transactions
- View total income and expenses
- Visualize transaction data using charts

## Tech Stack
- Backend: Spring Boot 3.5.0
- Frontend: React with Tailwind CSS
- Database: PostgreSQL
- Authentication: Spring Security with Basic Auth
- Testing: JUnit 5, Mockito

## Installation

### Prerequisites
- Java 17 or higher
- Node.js and npm
- PostgreSQL
- Maven

### Backend Setup
1. Clone the repository
2. Configure PostgreSQL database in `application.properties`
3. Run the following commands:
```bash
mvn clean install
mvn spring-boot:run
```

### Frontend Setup
1. Navigate to the frontend directory:
```bash
cd frontend
```
2. Install dependencies:
```bash
npm install
```
3. Start the development server:
```bash
npm run dev
```

## Project Structure
```
financy/
├── src/
│   ├── main/
│   │   ├── java/com/financy/financy/
│   │   │   ├── auth/           # Authentication module
│   │   │   ├── transaction/    # Transaction module
│   │   │   └── config/         # Configuration classes
│   │   └── resources/
│   └── test/                   # Test files
├── frontend/
│   ├── src/
│   │   ├── components/         # React components
│   │   ├── pages/             # Page components
│   │   └── context/           # React context
│   └── public/
└── pom.xml                     # Maven configuration
```

## API Documentation

### Authentication Endpoints

#### Register User
- **POST** `/auth/register`
- **Body:**
```json
{
    "username": "string",
    "password": "string"
}
```

#### Login
- **POST** `/auth/login`
- **Body:**
```json
{
    "username": "string",
    "password": "string"
}
```

### Transaction Endpoints

#### Create Transaction
- **POST** `/transactions`
- **Headers:** Basic Auth
- **Body:**
```json
{
    "amount": "number",
    "type": "INCOME|EXPENSE",
    "category": "string",
    "date": "datetime"
}
```

#### Get All Transactions
- **GET** `/transactions`
- **Headers:** Basic Auth

#### Get Transaction by ID
- **GET** `/transactions/{id}`
- **Headers:** Basic Auth

#### Get Transactions by Type
- **GET** `/transactions/type?type=INCOME|EXPENSE`
- **Headers:** Basic Auth

#### Update Transaction
- **PUT** `/transactions/{id}`
- **Headers:** Basic Auth
- **Body:** Same as Create Transaction

#### Delete Transaction
- **DELETE** `/transactions/{id}`
- **Headers:** Basic Auth

#### Get Total Income
- **GET** `/transactions/total-income`
- **Headers:** Basic Auth

#### Get Total Expenses
- **GET** `/transactions/total-expenses`
- **Headers:** Basic Auth

### Transaction Categories
#### Income Categories
- SALARY
- FREELANCE
- INVESTMENT
- BUSINESS
- OTHER_INCOME

#### Expense Categories
- FOOD
- TRANSPORTATION
- HOUSING
- UTILITIES
- ENTERTAINMENT
- SHOPPING
- HEALTHCARE
- EDUCATION
- TRAVEL
- OTHER_EXPENSE

## To-do List

**Backend**
- [x] Create auth module
- [x] Create transaction module
- [x] create test for auth module
- [x] create test for transaction module
- [x] fix add transaction req body (include date)
- [x] add transaction category type
- [x] show total income & expense
- [ ] create testing
- [ ] setup docker file
- [ ] setup github actions

**Frontend**
- [x] add tailwind css
- [x] Create home page
- [x] show transaction type using pie chart