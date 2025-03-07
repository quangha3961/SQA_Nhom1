package beebooks.service;

import beebooks.entities.*;
import beebooks.repository.CheckEmailRepository;
import beebooks.repository.ContactRepository;
import beebooks.repository.OrderRepository;
import beebooks.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public abstract class BaseService<E extends BaseEntity>{

	private static int SIZE_OF_PAGE = 20;

	@Autowired
	protected CheckEmailRepository checkEmailRepository;

	@Autowired
	protected ContactRepository contactRepository;

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected OrderRepository orderRepository;

	@PersistenceContext
	protected EntityManager entityManager;

	protected BaseService() {
	}

	protected abstract Class<E> clazz();

	@SneakyThrows
	@Transactional
	public E saveOrUpdate(E entity) {
		if (entity.getId() == null || entity.getId() <= 0) {
			entity.setCreatedDate(new Date());
			entity.setCreatedBy(1);
			entityManager.persist(entity); // thêm mới
			return entity;
		} else {
			// Kiểm tra xem cần cập nhật ngày tạo mới hay không
			E existingEntity = (E) entityManager.find(entity.getClass(), entity.getId());
			if (existingEntity != null) {
				entity.setCreatedDate(existingEntity.getCreatedDate());
				entity.setCreatedBy(existingEntity.getCreatedBy());
			}
			entity.setUpdatedDate(new Date());
			entity.setUpdatedBy(1);
			return entityManager.merge(entity); // cập nhật
		}
	}

	@Transactional
	public List<Subcribe> checkEmailSubcribe(Subcribe entity) {
		return checkEmailRepository.findByEmail(entity.getEmail());
	}

	@Transactional
	public List<Contact> checkEmailContact(Contact entityContact) {
		return contactRepository.findByEmailContact(entityContact.getEmail());
	}

	@Transactional
	public List<User> checkEmailRegister(User entityUser) {
		return userRepository.findByEmailRegister(entityUser.getEmail());
	}

	@Transactional
	public List<User> checkUserNameRegister(User entityUser){
		return userRepository.findByUserNameRegister(entityUser.getUsername());
	}

	@Transactional
	public List<Saleorder> checkEmailOrder(Saleorder entitySaleOrder) {
		return orderRepository.findByEmailOrder(entitySaleOrder.getCustomer_email());
	}

	public void delete(E entity) {
		entityManager.remove(entity);
	}

	public void deleteById(int primaryKey) {
		E entity = this.getById(primaryKey);
		delete(entity);
	}

	public E getById(int primaryKey) {
		return entityManager.find(clazz(), primaryKey);
	}

	public E getOneByNativeSQL(String sql) {
		try {
			return executeByNativeSQL(sql, 0).getData().get(0);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<E> findAll() {
		Table tbl = clazz().getAnnotation(Table.class);
		return (List<E>) entityManager.createNativeQuery("SELECT * FROM " + tbl.name(), clazz()).getResultList();
	}

	public PagerData<E> executeByNativeSQL(String sql, int page) {
		PagerData<E> result = new PagerData<E>();
		
		try {
			Query query = entityManager.createNativeQuery(sql, clazz());
			if(page > 0) {
				result.setCurrentPage(page);
				result.setTotalItems(query.getResultList().size());
				
				query.setFirstResult((page - 1) * SIZE_OF_PAGE);
				query.setMaxResults(SIZE_OF_PAGE);
			}
			
			result.setData(query.getResultList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int executeUpdateOrDeleteByNativeSQL(String sql) {
		try {
			return entityManager.createNativeQuery(sql).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
