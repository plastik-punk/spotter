package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import at.ac.tuwien.sepr.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.HashService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

@Profile({"generateData", "test"})
@Component
@Order(7)
public class EventDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_EVENTS_TO_GENERATE = 70;
    private static final int EVENTS_NEXT_12_MONTHS = 35;

    private final EventRepository eventRepository;
    private final HashService hashService;

    private static final String[] EVENT_NAMES = {
        "Summer Music Festival",
        "Local Artisan Market",
        "Tech Innovation Conference",
        "Food Truck Festival",
        "Charity Gala Dinner",
        "Outdoor Movie Night",
        "Fitness Bootcamp",
        "Wine Tasting Event",
        "Fashion Show Extravaganza",
        "Business Networking Breakfast",
        "Craft Beer Festival",
        "Pet Adoption Fair",
        "Community Volunteer Day",
        "Cooking Masterclass",
        "Yoga and Meditation Retreat",
        "Gardening Workshop",
        "Live Stand-up Comedy",
        "Photography Exhibition",
        "Book Club Meetup",
        "Antique Car Show",
        "Environmental Cleanup Drive",
        "Health and Wellness Expo",
        "Art Auction",
        "Dance Workshop",
        "Home Renovation Expo",
        "Music Album Launch Party",
        "Film Screening and Discussion",
        "Local Farmers' Market",
        "Student Hackathon",
        "Fashion Pop-up Sale",
        "Science Fair",
        "Open Mic Night",
        "Vintage Vinyl Record Fair",
        "Sports Skills Clinic",
        "Tech Startup Pitch Competition",
        "Pottery Making Workshop",
        "Nature Photography Hike",
        "Cook-off Challenge",
        "Writing Retreat",
        "Live Band Karaoke Night",
        "Game Development Workshop",
        "Local History Tour",
        "Fitness Challenge Event",
        "Artisanal Chocolate Tasting",
        "Caribbean Cuisine Festival",
        "Outdoor Adventure Race",
        "Pet Fashion Show",
        "Entrepreneurship Seminar",
        "Sustainability Panel Discussion",
        "Home Brewing Workshop",
        "Street Art Festival",
        "Charity Run and Fun Walk",
        "Concert Under the Stars",
        "Wellness Retreat",
        "Outdoor Survival Skills Workshop",
        "Vintage Fashion Fair",
        "Language Exchange Meetup",
        "Robotics Expo",
        "Craft Cocktail Mixology Class",
        "Bollywood Dance Night",
        "Mindfulness Meditation Session",
        "Virtual Reality Gaming Event",
        "DIY Home Improvement Workshop",
        "Local Music Showcase",
        "Futuristic Technology Symposium",
        "Urban Gardening Seminar",
        "Beer and BBQ Festival",
        "Comedy Improv Show",
        "Historical Reenactment Festival",
        "Virtual Art Gallery Opening",
        "Fashion Design Workshop"
    };

    private static final String[] EVENT_DESCRIPTIONS = {
        "Enjoy live performances from top artists and bands at our annual summer music festival.",
        "Discover unique handmade crafts and goods at our vibrant local artisan market.",
        "Explore the latest trends in technology and innovation with industry leaders and experts.",
        "Indulge in a variety of delicious cuisines from food trucks parked at our festival grounds.",
        "Join us for an elegant evening of fine dining and charity fundraising at our gala dinner.",
        "Watch classic movies under the stars with friends and family in our outdoor movie night.",
        "Challenge yourself with a full-body workout led by professional fitness instructors.",
        "Sample a selection of fine wines from around the world in our exclusive tasting event.",
        "Experience a showcase of cutting-edge fashion designs on our runway.",
        "Expand your professional network over breakfast and engaging discussions.",
        "Discover new flavors and enjoy craft beers from local breweries at our festival.",
        "Find your new furry friend and support animal adoption at our pet adoption fair.",
        "Give back to the community with a day of volunteering and making a positive impact.",
        "Learn culinary techniques from master chefs in our hands-on cooking masterclass.",
        "Relax and rejuvenate your mind and body with yoga and meditation in a tranquil setting.",
        "Learn how to cultivate a garden and grow your own plants in our gardening workshop.",
        "Laugh out loud with top comedians performing live stand-up comedy.",
        "Explore stunning photography exhibits from local and international artists.",
        "Discuss your favorite books and authors in our friendly book club meetup.",
        "Admire classic cars and vintage automobiles at our antique car show.",
        "Join us in cleaning up our environment and preserving nature's beauty.",
        "Discover the latest trends and products in health and wellness at our expo.",
        "Bid on original artworks and support local artists in our art auction.",
        "Learn new dance moves and techniques in our fun and energetic dance workshop.",
        "Explore home improvement ideas and meet experts at our home renovation expo.",
        "Celebrate the launch of a new music album with live performances and special guests.",
        "Watch thought-provoking films and engage in discussions with filmmakers.",
        "Shop for fresh produce and handmade goods at our farmers' market.",
        "Collaborate with peers and create innovative solutions in our student hackathon.",
        "Discover unique fashion pieces and accessories at our pop-up sale.",
        "Showcase your science project and learn from others at our science fair.",
        "Share your talent or enjoy performances at our open mic night.",
        "Browse and buy vinyl records from different genres at our record fair.",
        "Improve your sports skills with coaching from professional athletes.",
        "Watch startups pitch their ideas to investors and industry experts.",
        "Create your own pottery masterpiece with guidance from experienced artists.",
        "Capture the beauty of nature through photography on our guided hike.",
        "Compete in a cooking challenge and showcase your culinary skills.",
        "Find inspiration and focus on your writing in our peaceful retreat.",
        "Sing your favorite songs with a live band backing you up in our karaoke night.",
        "Learn the basics of game development and create your own game.",
        "Explore the history and landmarks of our local area with knowledgeable guides.",
        "Take part in fitness activities and challenges at our fitness challenge event.",
        "Indulge in the rich flavors of artisanal chocolates from local chocolatiers.",
        "Experience the vibrant flavors and rhythms of Caribbean cuisine.",
        "Test your limits in an outdoor adventure race through challenging terrain.",
        "Dress up your pets and compete in our fun and adorable fashion show.",
        "Gain insights into entrepreneurship and startup success stories.",
        "Discuss sustainability practices and environmental initiatives with experts.",
        "Learn how to brew your own beer at home with our brewing workshop.",
        "Celebrate street art and urban culture with live art demonstrations.",
        "Run or walk for a cause and raise funds for charity in our charity run.",
        "Enjoy a concert of your favorite music genres under the open sky.",
        "Relax and recharge with yoga, spa treatments, and healthy cuisine at our retreat.",
        "Learn essential outdoor survival skills and techniques in our workshop.",
        "Shop for vintage fashion pieces and accessories at our fashion fair.",
        "Practice speaking different languages and meet new friends in our language exchange.",
        "Explore the latest advancements in robotics and AI at our robotics expo.",
        "Mix and shake cocktails like a pro with our mixology class.",
        "Dance the night away to Bollywood music and rhythms.",
        "Practice mindfulness and meditation to reduce stress and improve well-being.",
        "Experience the thrill of virtual reality gaming in our VR gaming event.",
        "Learn how to improve your home with DIY projects and renovations.",
        "Discover local musical talent and enjoy live performances.",
        "Discuss future trends and technologies shaping our world at our tech symposium.",
        "Learn how to create your own urban garden and grow fresh produce.",
        "Savor delicious BBQ dishes and craft beers at our beer and BBQ festival.",
        "Laugh and be entertained by spontaneous comedy performances in our improv show.",
        "Experience history come alive with reenactments, costumes, and displays.",
        "Explore artworks from talented artists in our virtual art gallery.",
        "Design and create your own fashion pieces with guidance from experts."
    };

    public EventDataGenerator(EventRepository eventRepository, HashService hashService) {
        this.eventRepository = eventRepository;
        this.hashService = hashService;
    }

    @PostConstruct
    private void generateEvents() {
        LOGGER.trace("generateEvents");
        if (eventRepository.count() > 0) {
            LOGGER.debug("Events have already been generated");
        } else {
            LOGGER.debug("Generating {} event entries", NUMBER_OF_EVENTS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_EVENTS_TO_GENERATE; i++) {
                Event event = generateEvent(i);
                LOGGER.debug("Generated event: {}", event);
                eventRepository.save(event);
            }
            LocalDateTime startTime = LocalDateTime.of(2024, 6, 28, 15, 0);
            LocalDateTime endTime = LocalDateTime.of(2024, 6, 29, 23, 0);
            String hashId = hashService.hashSha256(String.valueOf(startTime));
            Event event = Event.EventBuilder.anEvent()
                .withName("Semester Closing Party")
                .withDescription("Celebrate all your hard earned 4s and 5s with your fellow students at the semester closing party.")
                .withStartTime(startTime)
                .withEndTime(endTime)
                .withHashId(hashId)
                .build();
            eventRepository.save(event);
        }
    }

    private Event generateEvent(int index) {
        LocalDateTime startTime;
        LocalDateTime endTime;
        if (index < EVENTS_NEXT_12_MONTHS) {
            // Event within the next 12 months
            startTime = LocalDateTime.now().plusMonths(index);
            endTime = startTime.plusDays(3);
        } else {
            // Event after the next 12 months
            startTime = LocalDateTime.now().plusMonths(12).plusMonths(index - EVENTS_NEXT_12_MONTHS);
            endTime = startTime.plusDays(3);
        }

        // Example of hashing the event index for a unique hash ID
        String hashId = hashService.hashSha256(String.valueOf(index));

        return Event.EventBuilder.anEvent()
            .withName(EVENT_NAMES[index])
            .withDescription(EVENT_DESCRIPTIONS[index])
            .withStartTime(startTime)
            .withEndTime(endTime)
            .withHashId(hashId)
            .build();
    }
}