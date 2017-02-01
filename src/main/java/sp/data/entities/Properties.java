package sp.data.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;


@Entity
@Table(name="properties")
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Properties {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;

    @NotNull
    @DecimalMin(value = "0", message = "{properties.percent.outOfRange}")
    @DecimalMax(value = "1", message = "{properties.percent.outOfRange}")
	@Column(name="percent_sp")
	private BigDecimal percentSp;

    @NotNull
    @DecimalMin(value = "0", message = "{properties.percent.outOfRange}")
    @DecimalMax(value = "1", message = "{properties.percent.outOfRange}")
	@Column(name="percent_discount")
	private BigDecimal percentDiscount;

    @NotNull
    @DecimalMin(value = "0", message = "{properties.percent.outOfRange}")
    @DecimalMax(value = "1", message = "{properties.percent.outOfRange}")
	@Column(name="percent_bank_commission")
	private BigDecimal percentBankCommission;


	public Properties() {
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public BigDecimal getPercentSp() {
        return percentSp;
    }

    public void setPercentSp(BigDecimal percentSp) {
        this.percentSp = percentSp != null ? percentSp.setScale(2, RoundingMode.HALF_DOWN) : null;
    }

    public BigDecimal getPercentDiscount() {
        return percentDiscount;
    }

    public void setPercentDiscount(BigDecimal percentDiscount) {
        this.percentDiscount = percentDiscount != null ? percentDiscount.setScale(2, RoundingMode.HALF_DOWN) : null;
    }

    public BigDecimal getPercentBankCommission() {
        return percentBankCommission;
    }

    public void setPercentBankCommission(BigDecimal percentBankComission) {
        this.percentBankCommission = percentBankComission != null ? percentBankComission.setScale(2, RoundingMode.HALF_DOWN) : null;
    }
}
