package com.vasenin.workcube.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vasenin.workcube.domains.*;
import com.vasenin.workcube.misc.Role;
import com.vasenin.workcube.repositories.*;
import com.vasenin.workcube.services.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.Join;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.sql.ast.tree.predicate.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class UserController {

    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    PlaceService placeService;
    @Autowired
    PlaceRepository placeRepository;
    @Autowired
    BadgeService badgeService;
    @Autowired
    NoiseMeasurementRepository noiseMeasurementRepository;
    @Autowired
    SecretCodeService secretCodeService;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    UserRepository userRepository;

    private List<MetroStation> listMetroStations;

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @PostConstruct
    public void init() {
        try {
            // Чтение файла JSON
            byte[] jsonData = Files.readAllBytes(Paths.get("src/main/resources/static/metros.json"));
            System.out.println("JSON data: " + new String(jsonData));
            // Преобразование JSON в список станций метро
            ObjectMapper objectMapper = new ObjectMapper();
            listMetroStations = objectMapper.readValue(jsonData, new TypeReference<List<MetroStation>>() {});
            System.out.println("Parsed Metro Stations: " + listMetroStations);
        } catch (IOException e) {
            listMetroStations = new ArrayList<>();
        }
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @GetMapping("/")
    public String getMainpage(){
        return "redirect:/main";
    }
    @GetMapping("/main")
    public String getMain(Model model, Authentication authentication){
        System.out.println(secretCodeService.generateSecretCode());
        if (authentication != null) {
            String username = authentication.getName();
            User user = userRepository.findByNickname(username);
            model.addAttribute("user", user);
        }
        List<Place> places = placeRepository.findAll(Sort.by(Sort.Direction.DESC, "rating"));

        String[] empty = {};
        model.addAttribute("isFree", false);
        model.addAttribute("startPrice", "");
        model.addAttribute("finalPrice", "");

        model.addAttribute("isAroundTheClock", false);
        model.addAttribute("startWeekdaysHours", "");
        model.addAttribute("finalWeekdaysHours", "");
        model.addAttribute("startWeekendHours", "");
        model.addAttribute("finalWeekendHours", "");

        model.addAttribute("search", "");
        model.addAttribute("filterWiFiCheck", false);
        model.addAttribute("filterQuietPlaceCheck", false);
        model.addAttribute("filterSocketsCheck", false);
        model.addAttribute("filterSeatsCheck", false);
        model.addAttribute("filterLocationCheck", false);
        model.addAttribute("listMetroStations", listMetroStations);
        model.addAttribute("metroUnavailable", listMetroStations.isEmpty());
        model.addAttribute("selectedTypes", empty);
        model.addAttribute("selectedMetroStations", empty);
        model.addAttribute("places", places);
        return "main_list";
    }
    @GetMapping("/filter")
    public String filterMain(
                            @RequestParam(value = "view", required = false) String view,
                            @RequestParam(value = "types", required = false) String[] types,
                             @RequestParam(value = "metroStations", required = false) String[] metroStations,

                             @RequestParam(value = "isFree", required = false) boolean isFree,
                             @RequestParam(value = "startPrice", required = false) String startPrice,
                             @RequestParam(value = "finalPrice", required = false) String finalPrice,

                             @RequestParam(value = "isAroundTheClock", required = false) boolean isAroundTheClock,
                             @RequestParam(value = "startWeekdaysHours", required = false) String startWeekdaysHours,
                             @RequestParam(value = "finalWeekdaysHours", required = false) String finalWeekdaysHours,
                             @RequestParam(value = "startWeekendHours", required = false) String startWeekendHours,
                             @RequestParam(value = "finalWeekendHours", required = false) String finalWeekendHours,

                             @RequestParam(value = "search", required = false) String search,

                             @RequestParam(value = "filterWiFiCheck", required = false) boolean filterWiFiCheck,
                             @RequestParam(value = "filterQuietPlaceCheck", required = false) boolean filterQuietPlaceCheck,
                             @RequestParam(value = "filterSocketsCheck", required = false) boolean filterSocketsCheck,
                             @RequestParam(value = "filterSeatsCheck", required = false) boolean filterSeatsCheck,

                             @RequestParam(value = "filterLocationCheck", required = false) boolean filterLocationCheck,
                             @RequestParam(value = "filterLatitude", required = false) String filterLatitude,
                             @RequestParam(value = "filterLongitude", required = false) String filterLongitude,
                             Model model, Authentication authentication) throws JsonProcessingException {

        if (authentication != null) {
            String username = authentication.getName();
            User user = userRepository.findByNickname(username);
            model.addAttribute("user", user);
        }

        Specification<Place> specification = Specification.where(null);

        specification = specification.and(FiltersSpecification.filter(
                types,
                metroStations,
                isFree,
                startPrice,
                finalPrice,
                isAroundTheClock,
                startWeekdaysHours,
                finalWeekdaysHours,
                startWeekendHours,
                finalWeekendHours,
                filterWiFiCheck,
                filterQuietPlaceCheck,
                filterSocketsCheck));

        List<Place> results = placeRepository.findAll(specification);

        for(Place place : results){
            System.out.println(place.getBadges());
        }

        boolean isLocationOn = filterLatitude != "" && filterLongitude != "";

        List<Place> searchedResults = results.stream()
                .filter(e -> search == null || e.getName().toLowerCase().contains(search.toLowerCase())).toList();

        List<Place> filteredPlaces = new ArrayList<>();
        if(isLocationOn){
            double latitudeConverted = Double.parseDouble(filterLatitude);
            double longitudeConverted = Double.parseDouble(filterLongitude);
            for(Place place : searchedResults){
                if(CalculatorForCoordinates.isPointInArea(latitudeConverted, longitudeConverted, place.getLatitude(), place.getLongitude(), 1000)){
                    filteredPlaces.add(place);
                }
            }
        }

        if(types == null){
            String[] newTypes = {};
            model.addAttribute("selectedTypes", newTypes);
        }else {
            model.addAttribute("selectedTypes", types);
        }
        if(metroStations == null){
            String[] newTypes = {};
            model.addAttribute("selectedMetroStations", newTypes);
        }else {
            model.addAttribute("selectedMetroStations", metroStations);
        }
        model.addAttribute("isFree", isFree);
        model.addAttribute("startPrice", startPrice == null ? "" : startPrice);
        model.addAttribute("finalPrice", finalPrice == null ? "" : finalPrice);

        model.addAttribute("isAroundTheClock", isAroundTheClock);
        model.addAttribute("startWeekdaysHours", startWeekdaysHours == null ? "" : startWeekdaysHours);
        model.addAttribute("finalWeekdaysHours", finalWeekdaysHours == null ? "" : finalWeekdaysHours);
        model.addAttribute("startWeekendHours", startWeekendHours == null ? "" : startWeekendHours);
        model.addAttribute("finalWeekendHours", finalWeekendHours == null ? "" : finalWeekendHours);

        model.addAttribute("search", search);
        model.addAttribute("filterWiFiCheck", filterWiFiCheck);
        model.addAttribute("filterQuietPlaceCheck", filterQuietPlaceCheck);
        model.addAttribute("filterSocketsCheck", filterSocketsCheck);
        model.addAttribute("filterSeatsCheck", filterSeatsCheck);
        model.addAttribute("filterLocationCheck", filterLocationCheck);
        model.addAttribute("listMetroStations", listMetroStations);
        model.addAttribute("metroUnavailable", listMetroStations.isEmpty());

        if(view != null && Objects.equals(view, "list")){
            if(isLocationOn){
                model.addAttribute("places", filteredPlaces);
            }else{
                model.addAttribute("places", searchedResults);
            }
            return "main_list";
        }else if(view != null && Objects.equals(view, "map")){

            String json;
            if(isLocationOn){
                model.addAttribute("latitude", filterLatitude);
                model.addAttribute("longitude", filterLongitude);
                model.addAttribute("showMe", true);
                json = ow.writeValueAsString(filteredPlaces);
            }else{
                json = ow.writeValueAsString(searchedResults);
            }
            model.addAttribute("places", json);

            return "main_map";
        }
        return "main_list";

    }

    @GetMapping("/main_map")
    public String mainMap(Model model) throws JsonProcessingException {
        List<Place> places = placeRepository.findAll();
        String json = ow.writeValueAsString(places);

        String[] empty = {};

        model.addAttribute("selectedTypes", empty);
        model.addAttribute("selectedMetroStations", empty);

        model.addAttribute("search", "");
        model.addAttribute("filterWiFiCheck", false);
        model.addAttribute("filterQuietPlaceCheck", false);
        model.addAttribute("filterSocketsCheck", false);
        model.addAttribute("filterSeatsCheck", false);
        model.addAttribute("filterLocationCheck", false);
        model.addAttribute("listMetroStations", listMetroStations);
        model.addAttribute("metroUnavailable", listMetroStations.isEmpty());
        model.addAttribute("places", json);

        return "main_map";
    }



    @PostMapping("/map_me")
    public String mapMe(@RequestParam(value = "latitude") float latitude,
                        @RequestParam(value = "longitude") float longitude,
                        Model model) throws JsonProcessingException {
        List<Place> places = placeRepository.findAll();

        String[] empty = {};
        List<Place> filteredPlaces = new ArrayList<>();
        for(Place place : places){
            if(CalculatorForCoordinates.isPointInArea(latitude, longitude, place.getLatitude(), place.getLongitude(), 1000)){
                filteredPlaces.add(place);
            }
        }

        String json = ow.writeValueAsString(filteredPlaces);

        model.addAttribute("listMetroStations", listMetroStations);
        model.addAttribute("metroUnavailable", listMetroStations.isEmpty());


        String[] newTypes = {};
        model.addAttribute("selectedTypes", newTypes);
        model.addAttribute("selectedMetroStations", newTypes);
        model.addAttribute("selectedTypes", empty);
        model.addAttribute("selectedMetroStations", empty);
        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);
        model.addAttribute("showMe", true);
        model.addAttribute("places", json);

        return "main_map";
    }

    @GetMapping("/admin/create_place")
    public String getCreatePlace(Model model){
        String newSecretCode = secretCodeService.generateSecretCode();
        model.addAttribute("secretCode", newSecretCode);
        model.addAttribute("listMetroStations", listMetroStations);
        model.addAttribute("metroUnavailable", listMetroStations.isEmpty());
        return "create_place";
    }

    @PostMapping("/admin/create_place")
    public String createPlace(@RequestParam(value = "file", required = false) MultipartFile file,
                              @RequestParam(value = "namePlace") String name,
                              @RequestParam(value = "addressPlace") String address,
                              @RequestParam(value = "sitePlace", required = false) String site,
                              @RequestParam(value = "codePlace") String code,
                              @RequestParam(value = "typePlace") String type,
                              @RequestParam(value = "latitudePlace") String latitude,
                              @RequestParam(value = "longitudePlace") String longitude,
                              @RequestParam(value = "metroPlace", required = false) Set<String> metros,
                              @RequestParam(value = "isFreePlace", required = false) boolean isFree,
                              @RequestParam(value = "pricePlace") String price,
                              @RequestParam(value = "typePricePlace") String typePrice,
                              @RequestParam(value = "isACPlace", required = false) boolean isAroundTheClock,
                              @RequestParam(value = "startDaysPlace") String startWeekday,
                              @RequestParam(value = "finalDaysPlace") String finalWeekday,
                              @RequestParam(value = "startEndsPlace") String startWeekend,
                              @RequestParam(value = "finalEndsPlace") String finalWeekend,
                              Model model, Authentication authentication) throws IOException {
        if(authentication != null){
            List<ErrorMessage> errors = placeService.checkNewPlace(name, address, latitude, longitude, isFree, price, isAroundTheClock, startWeekday, finalWeekday, startWeekend, finalWeekend);
            if(errors.isEmpty()){

                Place place = new Place();

                if(file != null){
                    System.out.println(uploadPath);
                    File uploadDir = new File(uploadPath);
                    if(!uploadDir.exists()){
                        uploadDir.mkdir();
                    }

                    String uuidFile = UUID.randomUUID().toString();
                    String resultFilename = uuidFile + "." + file.getOriginalFilename();

                    file.transferTo(new File(uploadPath + "/" + resultFilename));
                    place.setPhoto(resultFilename);
                }else{
                    place.setPhoto("defaultPlace.jpg");
                }

                place.setName(name);
                place.setAddress(address);
                place.setSite(site);
                place.setSecretCode(code);
                place.setType(type);
                place.setLatitude(Double.parseDouble(latitude));
                place.setLongitude(Double.parseDouble(longitude));
                place.setMetroStations(metros);
                place.setFree(isFree);
                if(isFree){
                    price = "0";
                    typePrice = "";
                }
                place.setPrice(Integer.parseInt(price));
                place.setTypePrice(typePrice);
                place.setAroundTheClock(isAroundTheClock);
                if(isAroundTheClock){
                    startWeekday = "0";
                    finalWeekday = "0";
                    startWeekend = "0";
                    finalWeekend = "0";
                }
                place.setStartWorkingHoursWeekdays(Integer.parseInt(startWeekday));
                place.setFinishWorkingHoursWeekdays(Integer.parseInt(finalWeekday));
                place.setStartWorkingHoursWeekends(Integer.parseInt(startWeekend));
                place.setFinishWorkingHoursWeekends(Integer.parseInt(finalWeekend));
                placeRepository.save(place);
                return "redirect:/admin/create_place";
            }else{
                for (ErrorMessage error : errors) {
                    model.addAttribute(error.getType().getValue(), error);
                    error.print();
                }
                model.addAttribute("name", name);
                model.addAttribute("address", address);
                model.addAttribute("site", site);
                model.addAttribute("secretCode", code);
                model.addAttribute("type", type);
                model.addAttribute("latitudePlace", latitude);
                model.addAttribute("longitudePlace", longitude);
                model.addAttribute("metros", metros);
                model.addAttribute("isFree", isFree);
                model.addAttribute("price", price);
                model.addAttribute("typePrice", typePrice);
                model.addAttribute("isAC", isAroundTheClock);
                model.addAttribute("startWeek", startWeekday);
                model.addAttribute("finalWeek", finalWeekday);
                model.addAttribute("startEnd", startWeekend);
                model.addAttribute("finalEnd", finalWeekend);
                model.addAttribute("listMetroStations", listMetroStations);
                model.addAttribute("metroUnavailable", listMetroStations.isEmpty());
                return "create_place";
            }
        }
        return "redirect:/main";
    }


    @GetMapping("/place/{placeId}")
    public String getPlacePage( @PathVariable(value = "placeId")long placeId,
                                Model model, Authentication authentication) throws JsonProcessingException {
        if (authentication != null) {
            String name = authentication.getName();
            User user = userRepository.findByNickname(name);
            Role userRole = user.getRole();
            String role = userRole.getAuthority();
            boolean isAdmin = role.equalsIgnoreCase("ROLE_ADMIN");
            model.addAttribute("user", user);
            model.addAttribute("isAdmin", isAdmin);
        }

        Place place = placeRepository.findById(placeId);
        List<Review> reviews = reviewRepository.findAllByPlaceIdOrderByDateDesc(placeId);

        List<NoiseMeasurement> noiseMeasurements = noiseMeasurementRepository.findAllBySecretCode(place.getSecretCode());
        if(!noiseMeasurements.isEmpty()) {

            ArrayList<String> times = new ArrayList<>();
            ArrayList<Double> points = new ArrayList<>();
            times.add("0");
            points.add(0.0);

            for (NoiseMeasurement noiseMeasurement : noiseMeasurements) {
                times.add(noiseMeasurement.getFormatedDateTime());
                points.add(noiseMeasurement.getNoiseLevel());
            }

            String jsonTimes = ow.writeValueAsString(times);
            String jsonPoints = ow.writeValueAsString(points);

            model.addAttribute("times", jsonTimes);
            model.addAttribute("points", jsonPoints);
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("place", place);

        model.addAttribute("listMetroStations", listMetroStations);
        model.addAttribute("metroUnavailable", listMetroStations.isEmpty());
        return "place_page";
    }
    @GetMapping("/user/new_review/{placeId}")
    public String getNewReviewPage(@PathVariable(value = "placeId")long placeId,
                                   Model model, Authentication authentication) {
        if (authentication != null) {
            String name = authentication.getName();
            User user = userRepository.findByNickname(name);
            model.addAttribute("user", user);
            Place place = placeRepository.findById(placeId);
            model.addAttribute("place", place);
            model.addAttribute("listMetroStations", listMetroStations);
            model.addAttribute("metroUnavailable", listMetroStations.isEmpty());
        }
        return "new_review";
    }
    @PostMapping("/user/new_review/post/{placeId}")
    public String getNewReviewPage(@PathVariable(value = "placeId") long placeId,
                                   @RequestParam(value = "seats") int seats,
                                   @RequestParam(value = "location") int location,
                                   @RequestParam(value = "friendly") int friendly,
                                   @RequestParam(value = "aethtetic") int aethtetic,
                                   @RequestParam(value = "sockets") int sockets,
                                   @RequestParam(value = "wifi") int wifi,
                                   @RequestParam(value = "noise") int noise,
                                   @RequestParam(value = "comment", required = false) String comment,
                                   Model model, Authentication authentication) {
        if (authentication != null) {
            String name = authentication.getName();
            User user = userRepository.findByNickname(name);
            Place place = placeRepository.findById(placeId);
            Review review = new Review();
            review.setUser(user);
            review.setPlace(place);
            review.setSeatRating(seats);
            review.setLocationRating(location);
            review.setStaffRating(friendly);
            review.setAethteticRating(aethtetic);
            review.setSocketRating(sockets);
            review.setWifiRating(wifi);
            review.setNoiseRating(noise);
            review.setComment(comment);
            review.setDate(new Date());
            reviewRepository.save(review);

            placeService.calculateRating(placeId);
            badgeService.processReviews(placeId);

            return "redirect:/place/{placeId}";
        }else{
            return "redirect:/signin";
        }
    }

    @GetMapping("/user/profile")
    public String getProfilePage(Model model, Authentication authentication){
        if (authentication != null) {
            String name = authentication.getName();
            User user = userRepository.findByNickname(name);
            Role userRole = user.getRole();
            List<Review> reviews = reviewRepository.findAllByUserIdOrderByDateDesc(user.getId());
            String role = userRole.getAuthority();
            boolean isAdmin = role.equalsIgnoreCase("ROLE_ADMIN");
            model.addAttribute("user", user);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("reviews", reviews);

            return "profile";
        }
        return "redirect:/signin";
    }

    @GetMapping("/signin")
    public String getSignInPage(@RequestParam(value = "error", required = false) String error,
                                Model model){
        if(error != null){
            model.addAttribute("error", "Введены некорректные данные");
        }
        return "sign_in";
    }
    @GetMapping("/signup")
    public String getSignUpPage(){ return "sign_up"; }

    @GetMapping("/logout")
    public String logoutPage(){ return "redirect:/main"; }


    @GetMapping("/delete_review/{reviewId}/{userId}")
    public String deleteReview(@PathVariable(value = "reviewId")long reviewId,
                               @PathVariable(value = "userId")long userId,
                               Authentication authentication,
                               HttpServletRequest request){
        if(authentication != null){
            User user = userRepository.findByNickname(authentication.getName());
            if((user.getId() == userId) || (user.getRole().getAuthority().equals("ROLE_ADMIN"))){
                Review review = reviewRepository.findById(reviewId);
                Place place = review.getPlace();
                reviewRepository.delete(review);

                placeService.calculateRating(place.getId());
                badgeService.processReviews(place.getId());
            }
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/registration")
    public String signUp(@RequestParam(value = "file", required = false) MultipartFile file,
                         @RequestParam(value = "nickname") String nickname,
                         @RequestParam(value = "name") String name,
                         @RequestParam(value = "surname") String surname,
                         @RequestParam(value = "email") String email,
                         @RequestParam(value = "password") String password,
                         @RequestParam(value = "repeatPassword") String repeatPassword,
                         Model model) throws IOException {
        List<ErrorMessage> errors = authenticationService.checkRegistration(nickname, name, surname, email, password, repeatPassword);
        if(errors.isEmpty()){
            User user = new User();
            if(file != null){
                System.out.println(uploadPath);
                File uploadDir = new File(uploadPath);
                if(!uploadDir.exists()){
                    uploadDir.mkdir();
                }

                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();

                file.transferTo(new File(uploadPath + "/" + resultFilename));
                user.setPhotopath(resultFilename);
            }else{
                user.setPhotopath("defaultUser.jpg");
            }
            user.setNickname(nickname);
            user.setName(name);
            user.setSurname(surname);
            user.setEmail(email);
            user.setPassword(passwordEncoder().encode(password));
            user.setRole(Role.USER);
            userRepository.save(user);

            return "redirect:/signin";
        }else {
            for (ErrorMessage error : errors) {
                model.addAttribute(error.getType().getValue(), error);
                error.print();
            }
            model.addAttribute("nickname", nickname);
            model.addAttribute("name", name);
            model.addAttribute("surname", surname);
            model.addAttribute("email", email);
            model.addAttribute("password", password);
            model.addAttribute("repeatPassword", repeatPassword);
            return "sign_up";
        }
    }

}
