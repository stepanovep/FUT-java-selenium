package stepanovep.fut23.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import stepanovep.fut23.core.entity.FutPlayerElement;
import stepanovep.fut23.core.page.club.ClubPlayersPage;
import stepanovep.fut23.core.page.club.ClubPlayersSearchOptions;
import stepanovep.fut23.core.page.transfers.SearchResult;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class GemsSeller {

    private final ClubPlayersPage clubPlayersPage;

    public void sellGems() {
        ClubPlayersSearchOptions searchOptions = ClubPlayersSearchOptions.builder().build();
        SearchResult searchResult = clubPlayersPage.searchPlayers(searchOptions);
        List<FutPlayerElement> players = searchResult.getPlayers();
        for (FutPlayerElement player: players) {
            List<Integer> prices = player.comparePrice();
        }
        players.size();
    }
}
