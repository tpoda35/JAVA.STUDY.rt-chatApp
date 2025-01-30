package com.rt_chatApp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_usernameGenNum")
public class UsernameGenNum {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer num;
}
