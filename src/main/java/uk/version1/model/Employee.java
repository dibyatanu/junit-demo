package uk.version1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public final class Employee {
   @JsonIgnore
   @Schema(hidden = true)
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long  id;
   private String name;
   private LocalDateTime timeStamp;

}
