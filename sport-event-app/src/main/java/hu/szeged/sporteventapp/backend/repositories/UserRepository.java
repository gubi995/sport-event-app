package hu.szeged.sporteventapp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.szeged.sporteventapp.backend.data.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

	User findByUsername(String username);

	int countByUsername(String username);

	int countByEmail(String email);

	@Modifying
	@Query("UPDATE User user SET user.age = :age, user.realName = :realName, user.mobileNumber = :mobileNumber WHERE user.id = :id")
	int updateUserAdditionalData(
			@Param("id") String id,
			@Param("age") int age,
			@Param("realName") String realName,
			@Param("mobileNumber") String mobileNumber);

	@Modifying
	@Query("UPDATE User user SET user.password = :password WHERE user.id = :id")
	int updateUserPassword(
			@Param("id") String id,
			@Param("password") String password);

	@Modifying
	@Query("UPDATE User user SET user.pictureName = :pictureName WHERE user.id = :id")
	int updateUserImageData(
			@Param("id") String id,
			@Param("pictureName") String pictureName);
}
