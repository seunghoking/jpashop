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

### em.persist 할 때...
영속성 컨텍스트에는 키와 밸류가 있는데 persist할 때의 key가
저장한 해당 객체의 PK값이 된다.

### Service 계층에서 JPA의 모든 데이터 변경이나 로직들은...?
@Transactional 이 필요하다.
그래야 Lazy 로딩 이런 것들이 되고 꼭 필요하다.
- 참고 : 조회 쪽에서 @transactional을 쓸 때 readonly = true 옵션을 주면 최적화가 가능하다.

### 중복 회원 검증 비지니스 로직에서의 주의점
만약 중복 회원 검증을 할 때 동시에 같은 이름의 회원이 회원가입을 한다면 둘다 등록이 되어 버린다.
따라서 실무에서는 데이터베이스의 이름에 unique 제약조건을 추가하는 것이 바람직할 것이다.  

### 필드 주입, 세터 주입의 문제
필드 주입은 변경이 일단 되지 않는다. 어떠한 경우에 변경할 사항이 있을 수 있는데 불가하다.
그래서 세터 주입을 쓸 수 있는데, 세터 주입은 Mock같은 것들을 주입할 수 있다는 것이 장점이다. 그러나 한번 무언가 런타임에 누군가가 이걸 바꾼다면 치명적일 것이다.

-> 궁극적으로 생성자 인젝션이 좋다. @RequiredArgsConstructor는 final이 붙은 것들, @AllArgsConstructor는 final 말고도 다.
Entity 매니저 또한 스프링 부트안에서는 @Autowired로 사용가능하다.

### 기능 테스트 시에 insert문이 발생안한다?
- persist한다고 해서 DB에 insert가 안된다. db트랜잭션 commit될때 그 때 이제 flush가 되면서 db에 insert query가 나감.
- 그런데 스프링에서 @Transactional은 기본적으로 커밋을 안하고 롤백을 해버림 그래서 @Rollback(false)를 하면 됨.
- 만약 롤백 방식으로 하고도 쿼리를 확인하고 싶으면 EntityManager를 만들고 Em.flush()를 해주면 된다.