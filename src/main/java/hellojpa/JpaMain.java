package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        // 하나만 생성해서 애플리케이션 전체 공유
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 쓰레드마다 생성. 쓰레드간에 공유X
        EntityManager em = emf.createEntityManager();

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
        /*
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
        */
        // find & modify
/*        try {
            BMember findMember = em.find(BMember.class, 1L);
//            findMember.setName("HelloJpa");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }*/
        // delete
        /*
        try {
            BMember findMember = em.find(BMember.class, 2L);
            em.remove(findMember);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        */

        emf.close();
    }
}
