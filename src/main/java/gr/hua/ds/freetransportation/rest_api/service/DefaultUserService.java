package gr.hua.ds.freetransportation.rest_api.service;

import gr.hua.ds.freetransportation.dao.UnemploymentApplicationRepository;
import gr.hua.ds.freetransportation.entities.UnemploymentApplication;
import gr.hua.ds.freetransportation.entities.User;
import gr.hua.ds.freetransportation.rest_api.Response;
import gr.hua.ds.freetransportation.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class DefaultUserService {

    @Autowired
    private UnemploymentApplicationRepository applicationRepository;

    public ResponseEntity<?> submit(UnemploymentApplication application) {
        User user = UserUtil.getCurrentUser();
        int pendingApplications = applicationRepository.countPendingApplications(user.getId());

        if (pendingApplications > 0) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "User: " + user.getFirstName() + " has already submitted an application");
//            return new ResponseEntity<>("User: " + user.getFirstName() + " has already submitted an application", HttpStatus.METHOD_NOT_ALLOWED);
        }

        application.setUser(user);
        applicationRepository.save(application);
        return ResponseEntity.ok(new Response(200, "Unemployment application successfully submitted."));
    }
}
