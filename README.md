# 실전! 스프링 부트와 JPA
강사 : 인프런 / 김영한 강사님

### Entity
DB의 테이블과 매칭이 되는 개념

### EntityManager
엔티티 매니저 내부에 *영속성 컨텍스트(Persistance Context)* 라는 걸 두어서 엔티티들을 관리함

> 영속성 컨텍스트란?
*영속성 컨텍스트를 관리하는 모든 엔티티 매니저가 초기화 및 종료되지 않는 한 엔티티를 영구히 저장하는 환경*

### Transactional
스프링에서 트랜잭션 어노테이션을 사용할때 Test코드에서는 실행이 끝나고 롤백을 해버린다. Test코드가 아니라면 롤백을 하지않는다.

### 도메인 모델과 테이블 설계
1. 되도록 설계 단계에서는 단방향으로 설계하라.
2. 다대다 관계는 사용하면 안됨. -> 일대다 다대일로 나눠서 사용

## 엔티티 설계시 주의점
### 엔티티에는 가급적 Setter를 사용하지 말자
- 변경 포인트가 너무 많아서 유지보수가 어렵다.
### 모든 연관관계는 지연로딩(LAZY)으로 설정!
- XToOne(OneToOne, ManyToOne) 들은 Default가 즉시로딩(EAGER)이다. 직접 가서 LAZY로 바꿔줘야함.
- 즉시로딩(EAGER)은 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어렵다. 특히 JPQL을 실행할 때 N+1 문제가 자주 발생한다.
### 컬렉션은 필드에서 초기화 하자

### 연관관계 편의 메서드
양방향 매핑 시 주의 할 점이 있는데 연관관계 편의 메서드를 통해 해결한다

## Cascade
기본적으로 entity는 각각 다 persist를 해줘야한다.
```java
persist(orderItemA)
persist(orderItemB)
persist(orderItemC)
persist(order)
// 원래는 이렇게 해야 하는데 Cascade를 사용하면
persist(order) 만 하면 된다.
```