package sp.data.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;


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
	private BigDecimal percentSp = new BigDecimal(0.15).setScale(2, BigDecimal.ROUND_HALF_DOWN);

    @NotNull
    @DecimalMin(value = "0", message = "{properties.percent.outOfRange}")
    @DecimalMax(value = "1", message = "{properties.percent.outOfRange}")
	@Column(name="percent_discount")
	private BigDecimal percentDiscount = new BigDecimal(0.03).setScale(2, BigDecimal.ROUND_HALF_DOWN);

    @NotNull
    @DecimalMin(value = "0", message = "{properties.percent.outOfRange}")
    @DecimalMax(value = "1", message = "{properties.percent.outOfRange}")
	@Column(name="percent_bank_commission")
	private BigDecimal percentBankCommission = new BigDecimal(0.01).setScale(2, BigDecimal.ROUND_HALF_DOWN);


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
        if (percentSp != null) this.percentSp = percentSp.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public BigDecimal getPercentDiscount() {
        return percentDiscount;
    }

    public void setPercentDiscount(BigDecimal percentDiscount) {
        if (percentDiscount != null) this.percentDiscount = percentDiscount.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public BigDecimal getPercentBankCommission() {
        return percentBankCommission;
    }

    public void setPercentBankCommission(BigDecimal percentBankComission) {
        if (percentBankComission != null) this.percentBankCommission = percentBankComission.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }
}
