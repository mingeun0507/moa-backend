package com.hanamja.moa.api.dto.group.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class KickOutRequestDto {

    @NotNull
    @JsonProperty(value = "group_id")
    private Long groupId;

    @NotNull
    @JsonProperty(value = "user_list")
    private List<Long> userList;

    @NotNull
    private String reason;

    @Nullable
    private String detail;
}
