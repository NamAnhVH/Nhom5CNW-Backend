package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.ReferenceFormDto;
import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Models.Reference;
import com.WebLearning.WebLearning.Repository.ReferenceRepository;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReferenceService {

    @Autowired
    private ReferenceRepository referenceRepository;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    public void addReference(ReferenceFormDto referenceDto) {
        Account account = authenticationFacade.getAccount();
        Reference reference = new Reference();
        reference.setTitle(referenceDto.getTitle());
        reference.setDescription(referenceDto.getDescription());
        reference.setUrlFile(referenceDto.getUrlFile().replace("view", "preview"));
        reference.setTime(LocalDateTime.now());
        reference.setAccount(account);
        referenceRepository.save(reference);
    }

    public ReferenceFormDto getById(Long id) {
        Reference reference = referenceRepository.findById(id).get();
        ReferenceFormDto referenceDto = new ReferenceFormDto();
        referenceDto.setId(reference.getId());
        referenceDto.setTitle(reference.getTitle());
        referenceDto.setUrlFile(reference.getUrlFile().replace("preview","view"));
        referenceDto.setDescription(reference.getDescription());
        return referenceDto;
    }

    public void updateReference(Long id, ReferenceFormDto referenceDto) {
        Reference reference = referenceRepository.findById(id).get();
        reference.setTitle(referenceDto.getTitle());
        reference.setDescription(referenceDto.getDescription());
        reference.setUrlFile(referenceDto.getUrlFile().replace("view","preview"));
        reference.setTime(LocalDateTime.now());
        referenceRepository.save(reference);
    }

    public List<ReferenceFormDto> getByCurrentAccount() {
        List<ReferenceFormDto> listReferenceDto = new ArrayList<>();
        Account account = authenticationFacade.getAccount();
        List<Reference> listReference = referenceRepository.findByAccountId(account.getId());
        for(Reference reference: listReference){
            ReferenceFormDto referenceDto = new ReferenceFormDto();
            referenceDto.setId(reference.getId());
            referenceDto.setTitle(reference.getTitle());
            referenceDto.setTime(formatLocalDateTimeToString(reference.getTime()));
            listReferenceDto.add(referenceDto);
        }
        return listReferenceDto;
    }

    public String formatLocalDateTimeToString(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime roundedTime = time.withNano(0);
        return roundedTime.format(formatter);
    }

    public void deleteReference(Long id) {
        referenceRepository.deleteById(id);
    }

    public boolean isAllowed(Long id) {
        Reference reference = referenceRepository.findById(id).get();
        return reference.getAccount().getId() == authenticationFacade.getAccount().getId();
    }

    public List<ReferenceFormDto> getTopSixReferenceAndAccountUnlocked() {
        List<ReferenceFormDto> listReferenceDto = new ArrayList<>();
        List<Reference> listReference = referenceRepository.findTop6ByAccountLockedFalseOrderByTimeDesc();
        for(Reference reference: listReference){
            ReferenceFormDto referenceDto = new ReferenceFormDto();
            referenceDto.setId(reference.getId());
            referenceDto.setTitle(reference.getTitle());
            referenceDto.setTime(formatLocalDateTimeToString(reference.getTime()));
            listReferenceDto.add(referenceDto);
        }
        return listReferenceDto;
    }

    public List<ReferenceFormDto> getAllByAccountUnlocked() {
        List<ReferenceFormDto> listReferenceDto = new ArrayList<>();
        List<Reference> listReference = referenceRepository.findByAccountLockedFalseOrderByTimeDesc();
        for(Reference reference: listReference){
            ReferenceFormDto referenceDto = new ReferenceFormDto();
            referenceDto.setId(reference.getId());
            referenceDto.setTitle(reference.getTitle());
            referenceDto.setTime(formatLocalDateTimeToString(reference.getTime()));
            listReferenceDto.add(referenceDto);
        }
        return listReferenceDto;
    }

    public ReferenceFormDto getReferenceById(Long id) {
        Reference reference = referenceRepository.findById(id).get();
        ReferenceFormDto referenceDto = new ReferenceFormDto();
        referenceDto.setTitle(reference.getTitle());
        referenceDto.setUrlFile(reference.getUrlFile());
        referenceDto.setTime(formatLocalDateTimeToString(reference.getTime()));
        referenceDto.setDescription(reference.getDescription());
        return referenceDto;
    }
}
