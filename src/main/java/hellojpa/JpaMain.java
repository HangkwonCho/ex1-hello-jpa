package hellojpa;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    // 영속성 테스트
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 장점
        // 1차 캐시 - 하나의 트랜잭션에서 발생(생성)후 삭제됨
        // 동일성(identity) 보장
        // 트랜잭션을 지원하는 쓰기 지연 (transactional write-behind)
        // 변경 감지(Dirty Checking)
        // 지연 로딩(Lazy Loading)
        try {
            //extractedV1(em);
            //extractedV2(em);
            //extractedV3(em);
            //extractedV4(em);
            //extractedV5(em);
            //entityMapping(em);
            //entityTestV1(em);

            //단방향
            //extractedTeamV1(em);

            //양방향 연관관계와 연관관계의 주인
            extractedBiDirectionalAssociation(em);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void extractedBiDirectionalAssociation(EntityManager em) {
        Team team = new Team();
        team.setName("TeamA");
        em.persist(team);

        Member member = new Member();
        member.setUsername("member1");
        member.setTeam(team);   // 단방향 연관관계 설정. 참조 저장
        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());
        List<Member> members = findMember.getTeam().getMembers();
        for (Member m : members) {
            System.out.println("m.getUsername() = " + m.getUsername());
        }
    }

    private static void extractedTeamV1(EntityManager em) {
        Team team = new Team();
        team.setName("TeamA");
        em.persist(team);

        Member member = new Member();
        member.setUsername("member1");
        member.setTeam(team);   // 단방향 연관관계 설정. 참조 저장
        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());
        System.out.println("findMember = " + findMember.getUsername());
        Team findTeam = findMember.getTeam();
        System.out.println("findTeam = " + findTeam.getName());

//        Member member = em.find(Member.class, 2L);
//        Team newTeam = em.find(Team.class, 3L);
//        member.setTeam(newTeam);
    }

    private static void entityTestV1(EntityManager em) {
        BMember memberA = new BMember();
        memberA.setUsername("A");

        BMember memberB = new BMember();
        memberB.setUsername("B");

        BMember memberC = new BMember();
        memberC.setUsername("C");


        System.out.println("=====================");
        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        System.out.println("memberA.id = " + memberA.getId());
        System.out.println("memberB.id = " + memberB.getId());
        System.out.println("memberC.id = " + memberC.getId());
        System.out.println("=====================");
        //em.clear();
    }

    // 엔티티 매핑
    private static void entityMapping(EntityManager em) {

        BMember member = em.find(BMember.class, 150L);
        //member.setName("A2222AAAA");
    }

    // 준영속
    private static void extractedV5(EntityManager em) {

        // 영속
        BMember member = em.find(BMember.class, 150L);
        //member.setName("A2222AAAA");

        // 준영속
        // em.detach(member);
        // em.clear();
        // em.close();
        // BMember member2 = em.find(BMember.class, 150L);

        System.out.println("=======================");
    }

    private static void extractedV4(EntityManager em) {
/*        BMember memberA = new BMember(300L, "memberA");
        BMember memberB = new BMember(301L, "memberB");
        BMember memberC = new BMember(302L, "memberC");

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);

        TypedQuery<BMember> query = em.createQuery("select m from BMember as m", BMember.class);
        List<BMember> resultList = query.getResultList();
        for (BMember member : resultList) {
            System.out.println("member.id = " + member.getId());
            System.out.println("member.name = " + member.getName());
        }*/
    }

    // 변경 감지 (Dirty Checking)
    // 1.commit 발생시 - flush()
    // 2.엔티티와 스냅샷 비교 (조회 시점의 정보를 스냅샷으로 관리함) - 비교해서 정보가 변경됐을 경우 3번 실행
    // 3.Update SQL 생성
    // 4.flush (1. em.flush(), 2. 트랜잭션 커밋, 3. JPQL 쿼리 실행)
    // 5.commit
    private static void extractedV3(EntityManager em) {
        //BMember member = em.find(BMember.class, 150L);
        //member.setName("AAAAA");
/*        BMember member = new BMember(202L, "member200");
        em.persist(member);
        em.flush();

        // flush 해도 조회는 1차캐시에서 조회된다.
        BMember findMember = em.find(BMember.class, 202L);
        System.out.println("findMember.id = " + findMember.getId());
        System.out.println("findMember.name = " + findMember.getName());

        System.out.println("===================");*/
    }

    // 트랜잭션을 지원하는 쓰기 지연
    private static void extractedV2(EntityManager em) {
/*        BMember member1 = new BMember(150L, "A");
        BMember member2 = new BMember(160L, "B");
        em.persist(member1);
        em.persist(member2);
        System.out.println("===================");*/
    }

    // 1차 캐시, 동일성 보장
    private static void extractedV1(EntityManager em) {
        // 엔티티를 생성한 상태 - 비영속
        // BMember member = new BMember();
        // member.setId(102L);
        // member.setName("HelloJPA");

        // 영속 - 1차 캐시에 저장됨
        // em.persist(member);
        // em.find(101L) 조회 시 1차 캐시에서 조회
        // em.find(1L) 조회 시 해당 트랜잭션에 포함되지 않았을 경우 DB에 조회후 1차 캐시에 저장.

        System.out.println("=== BEFORE ===");
        BMember findMember1 = em.find(BMember.class, 100L);
        BMember findMember2 = em.find(BMember.class, 100L);
        System.out.println("result = " + (findMember1 == findMember2));

        // 영속성 컨테이너에서 분리, 준영속 상태
        // em.detach(member);
        // 객체를 삭제한 상태(삭제)
        // em.remove(member);
        //System.out.println("=== AFTER ====");
    }
/*
    public static void main(String[] args) {

        // 하나만 생성해서 애플리케이션 전체 공유
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 쓰레드마다 생성. 쓰레드간에 공유X
        EntityManager em = emf.createEntityManager();

        // 영속성 컨텍스트
        // 1. 논리적인 개념
        // 2. 엔티티 매니저(EntityManager)를 통해서 영속성 컨텍스트에 접근

        // JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // JPQL - 객체를 대상으로 검색하는 객체 지향 쿼리
        // JPA는 SQL을 축상화한 JPQL이라는 객체 지향 쿼리 언어 제공, 특정 데이터베이스 SQL에 의존하지 않음.
        // SQL과 문법 유사
        // * 차이 *
        // JPQL은 엔티티 객체를 대상으로 쿼리
        // SQL은 데이터 베이스 테이블을 대상으로 쿼리
        try {
            List<BMember> result = em.createQuery("select m from BMember as m", BMember.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            for (BMember member : result) {
                System.out.println("member.name = " + member.getName());
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        // insert
        *//*
        try {
            BMember member = new BMember();
            member.setId(2L);
            member.setName("HelloB");
            em.persist(member);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        *//*
        // find & modify
*//*        try {
            BMember findMember = em.find(BMember.class, 1L);
//            findMember.setName("HelloJpa");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }*//*
        // delete
        *//*
        try {
            BMember findMember = em.find(BMember.class, 2L);
            em.remove(findMember);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        *//*

        emf.close();
    }*/
}
