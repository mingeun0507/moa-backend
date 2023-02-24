package com.hanamja.moa.api.dto.group.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class KickOutRequestDto {

    @NotNull
    private Long groupId;

    @NotNull
    private List<Long> userList;

    @NotNull
    private String reason;

    @Nullable
    private String detail;
}
