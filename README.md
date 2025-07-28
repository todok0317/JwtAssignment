# JWT Assignment

## 프로젝트 소개

Spring Boot를 이용한 JWT 기반 인증/인가 시스템 구현 프로젝트입니다.

JWT(JSON Web Token)를 활용하여 사용자 인증 및 권한 관리를 수행하며, 역할 기반 접근 제어를 통해 일반 사용자와 관리자를 구분합니다.

## 배포된 서비스

- **API 엔드포인트**: http://43.201.114.58:8080
- **Swagger UI**: http://43.201.114.58:8080/swagger-ui/index.html
- **GitHub Repository**: https://github.com/todok0317/JwtAssignment

## 기술 스택

**Backend**
- Java 17
- Spring Boot 3.3.0
- Spring Security
- Spring Data JPA
- JWT (JSON Web Token)

**Database**
- H2 Database (In-Memory)

**Test**
- JUnit 5
- MockMvc
- Spring Security Test

**Documentation**
- Swagger/OpenAPI 3

**Build Tool**
- Gradle

## 주요 기능

### 인증 시스템
- **회원가입**: 사용자명, 비밀번호, 닉네임으로 계정 생성
- **로그인**: JWT 토큰 발급 및 인증
- **비밀번호 암호화**: BCrypt 알고리즘 사용

### 권한 관리
- **USER**: 기본 사용자 권한
- **ADMIN**: 관리자 권한
- **역할 기반 접근 제어**: 관리자만 권한 부여 가능

### JWT 토큰 관리
- **토큰 생성 및 검증**: HS256 알고리즘 사용
- **토큰 만료**: 2시간 유효시간
- **Bearer 토큰**: Authorization 헤더 방식

## API 명세

### 인증 API

#### 회원가입
```http
POST /signup
Content-Type: application/json

{
  "username": "JIN HO",
  "password": "12341234",
  "nickname": "Mentos"
}
```

**응답**
```json
{
  "username": "JIN HO",
  "nickname": "Mentos",
  "roles": [
    {
      "role": "USER"
    }
  ]
}
```

#### 로그인
```http
POST /login
Content-Type: application/json

{
  "username": "JIN HO",
  "password": "12341234"
}
```

**응답**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 관리자 API

#### 관리자 권한 부여
```http
PATCH /admin/users/{userId}/roles
Authorization: Bearer {token}
```

**응답**
```json
{
  "username": "JIN HO",
  "nickname": "Mentos",
  "roles": [
    {
      "role": "ADMIN"
    }
  ]
}
```

## 프로젝트 구조

```
src/
├── main/
│   ├── java/com/example/jwtassignment/
│   │   ├── common/
│   │   │   ├── config/          # Swagger 설정
│   │   │   ├── error/           # 전역 예외 처리
│   │   │   ├── jwt/             # JWT 유틸리티
│   │   │   └── security/        # Spring Security 설정
│   │   ├── domain/
│   │   │   ├── user/            # 사용자 도메인
│   │   │   └── auth/            # 인증 도메인
│   │   └── JwtAssignmentApplication.java
│   └── resources/
│       ├── application.properties
│       └── application-test.properties
└── test/
    └── java/com/example/jwtassignment/
        └── domain/              # 테스트 코드
```

## 설치 및 실행

### 1. 프로젝트 클론
```bash
git clone https://github.com/todok0317/JwtAssignment.git
cd JwtAssignment
```

### 2. 애플리케이션 실행
```bash
# Gradle을 이용한 실행
./gradlew bootRun

# 또는 JAR 파일 빌드 후 실행
./gradlew build
java -jar build/libs/JwtAssignment-0.0.1-SNAPSHOT.jar
```

### 3. 접속 확인
- **애플리케이션**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **H2 Console**: http://localhost:8080/h2-console

## 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 테스트 결과 확인
./gradlew test --info
```

**테스트 커버리지**
- AuthController: 회원가입/로그인 성공/실패 케이스
- AdminController: 관리자 권한 부여 관련 케이스
- JWT 인증: 토큰 검증, 만료, 권한 확인

## 주요 설정

### JWT 설정
- **알고리즘**: HS256
- **토큰 유효시간**: 2시간
- **헤더 형식**: `Authorization: Bearer {token}`

### 보안 설정
- **비밀번호 암호화**: BCrypt
- **세션 정책**: STATELESS
- **CSRF**: 비활성화

### 데이터베이스
- **타입**: H2 In-Memory
- **DDL**: create-drop (애플리케이션 재시작 시 초기화)

## 배포

### AWS EC2 배포 완료
- EC2 인스턴스 생성 (Ubuntu 24.04)
- JDK 17 설치  
- 애플리케이션 배포
- 보안 그룹 설정 (포트 8080 오픈)
- 외부 접근 확인 완료

**배포된 서비스 접속 정보**
- **API 엔드포인트**: http://43.201.114.58:8080
- **Swagger UI**: http://43.201.114.58:8080/swagger-ui/index.html

## 개발 환경

### 필수 요구사항
- **Java SDK**: 17
- **Gradle**: 8.14.3
- **Spring Boot**: 3.3.0

### 환경변수 (선택사항)
```bash
# JWT Secret Key (기본값 사용 시 생략 가능)
export JWT_SECRET_KEY=your-secret-key-here
```
