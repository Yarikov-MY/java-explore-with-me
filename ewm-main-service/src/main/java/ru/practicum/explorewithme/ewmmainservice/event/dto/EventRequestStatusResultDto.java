package ru.practicum.explorewithme.ewmmainservice.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.explorewithme.ewmmainservice.request.dto.RequestDto;

import java.util.List;

@Data
@AllArgsConstructor
public class EventRequestStatusResultDto {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
