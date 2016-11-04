package sp.data.dao.hibernate.Interfaces;

public interface BasicCrudDaoTests {

    void GetById_InputNonExistentId_ShouldReturnNull();
    
    void getById_InputNull_ShouldReturnNull();

    void getAll_ShouldReturnAllEntitiesList();

    void saveAndGetById_InputExistingId_ShouldWriteAndReadFromDB();

    void save_InputNull_ShouldNotWriteToDB();

    void update_ShouldUpdateDB();

    void delete_ShouldDeleteFromDB();

    void delete_InputNull_ShouldNotDeleteFromDB();

    void deleteById_InputExistingId_ShouldDeleteFromDB();
    
    void deleteById_InputNonExistentId_ShouldNotDeleteFromDB();

    void deleteById_InputNULL_ShouldNotDeleteFromDB();
}
