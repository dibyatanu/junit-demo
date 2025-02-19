package uk.version1.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public final class Employee {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long  id;
   private String name;
   private LocalDateTime timeStamp;

}
