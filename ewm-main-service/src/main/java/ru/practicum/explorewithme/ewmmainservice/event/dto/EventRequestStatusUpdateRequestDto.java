package ru.practicum.explorewithme.ewmmainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.ewmmainservice.request.model.RequestStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequestDto {
    @NotNull
    private List<Integer> requestIds;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private RequestStatus status;
}
