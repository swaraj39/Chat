package com.example.SpringWebSocket.Repository;

import com.example.SpringWebSocket.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users,String> {
}
