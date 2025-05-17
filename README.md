# mini-spring-boot

스프링 프레임워크의 핵심 기능을 간단하게 구현해보는 프로젝트입니다. 의존성 주입, 애노테이션 기반 설정, 빈 관리 등 스프링의 주요 개념을 직접 코딩하며 학습할 수 있도록 구성되어 있습니다.

## ✨ 주요 기능

- @Component 애노테이션: 클래스를 빈으로 등록하기 위한 사용자 정의 애노테이션
- 의존성 주입: 생성자 또는 필드를 통한 의존성 주입 기능
- 빈 스캐닝: 특정 패키지 내의 클래스를 스캔하여 자동으로 빈으로 등록
- 애플리케이션 컨텍스트: 빈의 생성과 관리를 담당하는 컨텍스트 구현

## 🚀 실행 방법

```
git clone https://github.com/cheonkyu/mini-spring-boot.git
cd mini-spring-boot
./gradlew build
./gradlew run
```
