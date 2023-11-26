package be.poc.poc.automatic.maillist.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private String titel;
    private String email;
    private String functie;
}
