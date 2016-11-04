package sp.data.dao.hibernate.Interfaces;


public interface DaoTestsForEntitiesWithNameField {

    void getByName_InputExactName_ReturnOneEntity();

    void getByName_InputExactNameWrongCase_ReturnNull();

    void getByName_InputWrongName_ReturnNull();
    
    void getByName_InputNullName_ReturnNull();

    void searchByName_InputExistingFragment_ShouldReturnMatches();
    
    void searchByName_InputExistingFragment_ShouldReturnMax30Matches();

    void searchByName_InputExistingFragmentWrongCase_ShouldReturnMatches();

    void searchByName_InputNonExistentFragment_ShouldReturnEmptyList();

    void searchByName_InputNull_ShouldReturnNull();
}
