package sp.data.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name="properties")
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Properties {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="percent_sp")
	private BigDecimal percentSp;

	@Column(name="percent_discount")
	private BigDecimal percentDiscount;

	@Column(name="percent_bank_commission")
	private BigDecimal percentBankCommission;


	public Properties() {
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public BigDecimal getPercentSp() {
        return percentSp;
    }

    public void setPercentSp(BigDecimal percentSp) {
        this.percentSp = percentSp;
    }

    public BigDecimal getPercentDiscount() {
        return percentDiscount;
    }

    public void setPercentDiscount(BigDecimal percentDiscount) {
        this.percentDiscount = percentDiscount;
    }

    public BigDecimal getPercentBankCommission() {
        return percentBankCommission;
    }

    public void setPercentBankCommission(BigDecimal percentBankComission) {
        this.percentBankCommission = percentBankComission;
    }
}
