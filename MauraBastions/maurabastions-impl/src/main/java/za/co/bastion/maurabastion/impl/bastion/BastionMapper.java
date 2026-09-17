package za.co.bastion.maurabastion.impl.bastion;

import za.co.bastion.common.dto.Rank;
import za.co.bastion.fischl.FischlBastion;
import za.co.bastion.fischl.FischlDefender;
import za.co.bastion.fischl.FischlFacility;
import za.co.bastion.fischl.FischlHireling;
import za.co.bastion.maurabastion.api.bastion.MauraBastionBastionApi;

import java.util.ArrayList;
import java.util.List;

public final class BastionMapper {

    private BastionMapper() {
    }

    public static FischlBastion toFischlBastion(MauraBastionBastionApi.Bastion bastion) {
        List<FischlFacility> facilities = new ArrayList<>();
        if (bastion.getFacilities() != null) {
            facilities = bastion.getFacilities().stream().map(BastionMapper::toFischlFacility).toList();
        }
        List<FischlDefender> defenders = new ArrayList<>();
        if (bastion.getDefenders() != null) {
            defenders = bastion.getDefenders().stream().map(BastionMapper::toFischlDefender).toList();
        }
        List<FischlHireling> hirelings = new ArrayList<>();
        if (bastion.getHirelings() != null) {
            hirelings = bastion.getHirelings().stream().map(BastionMapper::toFischlHireling).toList();
        }
        return new FischlBastion()
                .setId(bastion.getId())
                .setUserId(bastion.getUserId())
                .setName(bastion.getName())
                .setDescription(bastion.getDescription())
                .setImgUrl(bastion.getImgUrl())
                .setDiscordThreadId(bastion.getDiscordThreadId())
                .setFacilities(facilities)
                .setDefenders(defenders)
                .setHirelings(hirelings);
    }

    public static MauraBastionBastionApi.Bastion toBastion(FischlBastion fischl) {
        List<MauraBastionBastionApi.Facility> facilities = new ArrayList<>();
        if (fischl.getFacilities() != null) {
            facilities = fischl.getFacilities().stream().map(BastionMapper::toFacility).toList();
        }
        List<MauraBastionBastionApi.Defender> defenders = new ArrayList<>();
        if (fischl.getDefenders() != null) {
            defenders = fischl.getDefenders().stream().map(BastionMapper::toDefender).toList();
        }
        List<MauraBastionBastionApi.Hireling> hirelings = new ArrayList<>();
        if (fischl.getHirelings() != null) {
            hirelings = fischl.getHirelings().stream().map(BastionMapper::toHireling).toList();
        }
        return new MauraBastionBastionApi.Bastion()
                .setId(fischl.getId())
                .setUserId(fischl.getUserId())
                .setName(fischl.getName())
                .setDescription(fischl.getDescription())
                .setImgUrl(fischl.getImgUrl())
                .setDiscordThreadId(fischl.getDiscordThreadId())
                .setFacilities(facilities)
                .setDefenders(defenders)
                .setHirelings(hirelings);
    }

    private static FischlFacility toFischlFacility(MauraBastionBastionApi.Facility f) {
        return new FischlFacility()
                .setId(f.getId())
                .setName(f.getName())
                .setRank(f.getRank() != null ? f.getRank().name() : null)
                .setMinimumRequiredHirelings(f.getMinimumRequiredHirelings())
                .setConstructionTurns(f.getConstructionTurns())
                .setCost(f.getCost())
                .setDescription(f.getDescription())
                .setImgUrl(f.getImgUrl())
                .setTable(f.getTable())
                .setConstructedTurns(f.getConstructedTurns())
                .setBastionId(f.getBastionId());
    }

    private static MauraBastionBastionApi.Facility toFacility(FischlFacility f) {
        return new MauraBastionBastionApi.Facility()
                .setId(f.getId())
                .setName(f.getName())
                .setRank(f.getRank() != null ? Rank.valueOf(f.getRank()) : null)
                .setMinimumRequiredHirelings(f.getMinimumRequiredHirelings())
                .setConstructionTurns(f.getConstructionTurns())
                .setCost(f.getCost())
                .setDescription(f.getDescription())
                .setImgUrl(f.getImgUrl())
                .setTable(f.getTable())
                .setConstructedTurns(f.getConstructedTurns())
                .setBastionId(f.getBastionId());
    }

    private static FischlDefender toFischlDefender(MauraBastionBastionApi.Defender d) {
        return new FischlDefender()
                .setId(d.getId())
                .setName(d.getName())
                .setDescription(d.getDescription())
                .setImgUrl(d.getImgUrl())
                .setType(d.getType())
                .setBastionId(d.getBastionId())
                .setAcquisitionStory(d.getAcquisitionStory());
    }

    private static MauraBastionBastionApi.Defender toDefender(FischlDefender d) {
        return new MauraBastionBastionApi.Defender()
                .setId(d.getId())
                .setName(d.getName())
                .setDescription(d.getDescription())
                .setImgUrl(d.getImgUrl())
                .setType(d.getType())
                .setBastionId(d.getBastionId())
                .setAcquisitionStory(d.getAcquisitionStory());
    }

    private static FischlHireling toFischlHireling(MauraBastionBastionApi.Hireling h) {
        return new FischlHireling()
                .setId(h.getId())
                .setName(h.getName())
                .setRole(h.getRole())
                .setDescription(h.getDescription())
                .setImgUrl(h.getImgUrl())
                .setBastionId(h.getBastionId())
                .setFacilityId(h.getFacilityId())
                .setAcquisitionStory(h.getAcquisitionStory());
    }

    private static MauraBastionBastionApi.Hireling toHireling(FischlHireling h) {
        return new MauraBastionBastionApi.Hireling()
                .setId(h.getId())
                .setName(h.getName())
                .setRole(h.getRole())
                .setDescription(h.getDescription())
                .setImgUrl(h.getImgUrl())
                .setBastionId(h.getBastionId())
                .setFacilityId(h.getFacilityId())
                .setAcquisitionStory(h.getAcquisitionStory());
    }
}
